package dataaccess;

import chess.ChessGame;
import model.Game;

import java.util.*;

public class MemoryGameDAO implements GameDAO {

    private final List<Game> gameData = new ArrayList<>();

    @Override
    public void clear() throws DataAccessException {
        try {
            gameData.clear();
        } catch (Exception e) {
            throw new DataAccessException("Failed to clear game records", e);
        }
    }

    @Override
    public int add(String gameName) throws DataAccessException {
        try {
            int gameID = gameData.size() + 1;
            gameData.add(new Game(gameID, null, null, gameName, new ChessGame()));
            return gameID;
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert game record", e);
        }
    }

    @Override
    public Game get(int gameID) throws DataAccessException {
        try {
            return gameData.get(gameID - 1);
        } catch (Exception e) {
            throw new DataAccessException("Failed to retrieve game record", e);
        }
    }

    @Override
    public List<Game> listGames() throws DataAccessException {
        try {
            return gameData;
        } catch (Exception e) {
            throw new DataAccessException("Failed to retrieve game records", e);
        }
    }

    @Override
    public void addUserToGame(
            int gameID,
            String username,
            ChessGame.TeamColor teamColor)
            throws DataAccessException {
        try {
            Game game = get(gameID);
            Game newGame = new Game(
                    gameID,
                    teamColor == ChessGame.TeamColor.WHITE ? username : game.whiteUsername(),
                    teamColor == ChessGame.TeamColor.BLACK ? username : game.blackUsername(),
                    game.gameName(),
                    game.game());
            gameData.add(gameID - 1, newGame);
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert user into the game record", e);
        }
    }
}
