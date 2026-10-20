package server;

import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import service.AuthService;
import service.GameService;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    private final AuthService authService;
    private final UserService userService;
    private final GameService gameService;

    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();

        authService = new AuthService(authDAO);
        userService = new UserService(userDAO);
        gameService = new GameService(gameDAO);

        javalin.exception(Exception.class, this::exceptionHandler);

        javalin.delete("/db", this::clearDB);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void exceptionHandler(Exception e, Context c) {
        switch (e) {
            default -> c.status(500).json(Map.of("msg", "Error: " + e.getMessage()));
        }
    }

}
