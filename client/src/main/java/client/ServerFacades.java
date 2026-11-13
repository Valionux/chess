package client;

import chess.ChessGame;
import model.Game;
import exceptions.AlreadyTakenException;
import exceptions.UnauthorizedException;

import java.util.List;

public interface ServerFacades {

    String register(String username, String password, String email) throws AlreadyTakenException;

    String login(String username, String password) throws UnauthorizedException;

    void logout() throws UnauthorizedException;

    void clear();

    int createGame(String gameName) throws UnauthorizedException;

    List<Game> listGames() throws UnauthorizedException;

    void joinGame(ChessGame.TeamColor color, int gameID) throws UnauthorizedException, AlreadyTakenException;
}
