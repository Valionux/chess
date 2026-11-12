package client;

import model.Game;
import service.AlreadyTakenException;
import service.UnauthorizedException;

import java.util.List;

public interface ServerFacade {

    void register(String username, String password, String email) throws UnauthorizedException, AlreadyTakenException;

    void login(String username, String password) throws UnauthorizedException;

    void logout() throws UnauthorizedException;

    void clear();

    void creatGame(String gameName);

    List<Game> listGames();

    void joinGame(int gameID, String color);
}
