package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.Auth;
import model.Game;
import request.CreateGameRequest;
import request.JoinGameRequest;

import java.util.ArrayList;
import java.util.List;

public class GameService {

    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public List<Game> listGame(String authToken) throws UnauthorizedException, DataAccessException {
        if (authDAO.get(authToken) == null) {
            throw new UnauthorizedException("Wrong authentification token");
        }

        List<Game> games = gameDAO.listGames();
        List<Game> gamesNoChess = new ArrayList<>();

        for (Game game : games) {
            gamesNoChess.add(
                    new Game(game.gameID(),
                            game.whiteUsername(),
                            game.blackUsername(),
                            game.gameName(),
                            null));
        }

        return gamesNoChess;
    }

    public int createGame(String authToken, CreateGameRequest req)
            throws DataAccessException,
            UnauthorizedException {
        String gameName = req.gameName();

        if (authDAO.get(authToken) == null) {
            throw new UnauthorizedException("Wrong authentification token");
        }

        if (gameName == null) {
            throw new IllegalArgumentException("A game name must be provided");
        }

        return gameDAO.add(gameName);
    }

    public void joinGame(String authToken, JoinGameRequest req)
            throws DataAccessException,
            UnauthorizedException,
            AlreadyTakenException {
        ChessGame.TeamColor playerColor = req.playerColor();
        int gameID = req.gameID();
        Auth auth = authDAO.get(authToken);

        if (auth == null) {
            throw new UnauthorizedException("Wrong authentification token");
        }

        if (playerColor == null) {
            throw new IllegalArgumentException("A color must be provided");
        }

        Game game;

        try {
            game = gameDAO.get(gameID);
        } catch (DataAccessException e) {
            if (e.getCause() instanceof IndexOutOfBoundsException) {
                throw new IllegalArgumentException("A game Id must be provided", e);
            }
            throw e;
        }

        String colorUsername = playerColor == ChessGame.TeamColor.WHITE ? game.whiteUsername() : game.blackUsername();

        if (colorUsername != null) {
            throw new AlreadyTakenException("Color already taken");
        }

        gameDAO.addUserToGame(gameID, auth.username(), playerColor);
    }
}
