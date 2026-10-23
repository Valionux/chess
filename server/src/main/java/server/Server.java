package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.json.JavalinGson;
import org.jetbrains.annotations.NotNull;
import request.LoginRequest;
import request.RegisterRequest;
import service.*;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;

    public Server() {
        javalin = Javalin.create(config -> {
            config.staticFiles.add("web");
            config.jsonMapper(new JavalinGson());
        });

        AuthDAO authDAO = new MemoryAuthDAO();
        UserDAO userDAO = new MemoryUserDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        clearService = new ClearService(authDAO, userDAO, gameDAO);
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        javalin.exception(Exception.class, this::exceptionHandler);

        javalin.delete("/db", this::clear);

        javalin.post("/user", this::register);

        javalin.post("/session", this::login);
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
            case IllegalArgumentException _, JsonSyntaxException _ ->
                    c.status(400).json(Map.of("message", "Error: bad request"));
            case AlreadyTakenException _ -> c.status(403).json(Map.of("message", "Error: already taken"));
            case UnauthorizedException _ -> c.status(401).json(Map.of("message", "Error: unauthorized"));
            default -> c.status(500).json(Map.of("msg", "Error: " + e.getMessage()));
        }
    }

    private void clear(@NotNull Context c) throws DataAccessException {
        clearService.clearDB();

        c.status(200);
    }

    private void register(@NotNull Context c)
            throws IllegalArgumentException,
            DataAccessException,
            AlreadyTakenException {
        var res = userService.register(c.bodyAsClass(RegisterRequest.class));
        c.status(200).json(res);
    }

    private void login(@NotNull Context c)
            throws DataAccessException,
            UnauthorizedException {
        var res = userService.login(c.bodyAsClass(LoginRequest.class));
        c.status(200).json(res);
    }
}
