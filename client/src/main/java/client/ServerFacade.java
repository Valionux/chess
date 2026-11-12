package client;

import chess.ChessGame;
import model.Game;
import service.AlreadyTakenException;
import service.UnauthorizedException;

import java.util.List;

public interface ServerFacade {

    void register(String username, String password, String email) throws AlreadyTakenException;

    void login(String username, String password) throws UnauthorizedException;

    void logout() throws UnauthorizedException;

    void clear();

    void createGame(String gameName) throws UnauthorizedException;

    List<Game> listGames() throws UnauthorizedException;

    void joinGame(ChessGame.TeamColor color, int gameID) throws UnauthorizedException, AlreadyTakenException;
}
