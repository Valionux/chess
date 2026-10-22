package server;

import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import request.RegisterRequest;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    private final ClearService clearService;
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

        clearService = new ClearService(authDAO, userDAO, gameDAO);
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        javalin.exception(Exception.class, this::exceptionHandler);

        javalin.delete("/db", this::clear);

        javalin.post("/user", this::register);
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

    private void clear(@NotNull Context c) throws DataAccessException {
        clearService.clearDB();

        c.status(200);
    }

    private void register(@NotNull Context c) {
        c.bodyAsClass(RegisterRequest.class);

    }
}
