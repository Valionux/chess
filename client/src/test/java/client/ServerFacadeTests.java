package client;

import chess.ChessGame;
import model.Game;
import org.junit.jupiter.api.*;
import server.Server;
import exceptions.AlreadyTakenException;
import exceptions.UnauthorizedException;

import java.util.List;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacades facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);

        facade = new ServerFacade(
                "http://localhost:" + port
        );

        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setUp() {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Nested
    class RegisterTests {
        @Test
        @DisplayName("Successful register")
        public void registerSuccess() throws AlreadyTakenException {
            Assertions.assertEquals("testUser",
                    facade.register(
                            "testUser",
                            "password123",
                            "test@email.com"
                    )
            );
        }

        @Test
        @DisplayName("Already taken username")
        public void registerUsernameAlreadyTaken() throws Exception {
            facade.register(
                    "testUser",
                    "password123",
                    "test@email.com"
            );

            Assertions.assertThrows(AlreadyTakenException.class, () ->
                    facade.register(
                            "testUser",
                            "password456",
                            "other@email.com"
                    )
            );
        }
    }

    @Nested
    class LoginTests {

        @Test
        @DisplayName("Successful login")
        public void loginSuccess() throws Exception {
            facade.register("testUser", "password123", "test@email.com");

            facade.logout();

            Assertions.assertEquals(
                    "testUser",
                    facade.login("testUser", "password123")
            );
        }

        @Test
        @DisplayName("Login with wrong username")
        public void loginWrongUsername() {
            Assertions.assertThrows(UnauthorizedException.class, () ->
                    facade.login("nonexistentUser", "password123")
            );
        }

        @Test
        @DisplayName("Login with wrong password")
        public void loginWrongPassword() throws Exception {
            facade.register("testUser", "password123", "test@email.com");
            facade.logout();

            Assertions.assertThrows(UnauthorizedException.class, () ->
                    facade.login("testUser", "wrongPassword")
            );
        }
    }

    @Nested
    class LogoutTests {

        @Test
        @DisplayName("Successful logout")
        public void logoutSuccess() throws Exception {
            facade.register("testUser", "password123", "test@email.com");

            Assertions.assertDoesNotThrow(() -> facade.logout());
        }

        @Test
        @DisplayName("Logging out without being logged it")
        public void logoutUnauthorized() {
            Assertions.assertThrows(UnauthorizedException.class, () ->
                    facade.logout()
            );
        }

        @Test
        @DisplayName("Logging out twice")
        public void logoutTwice() throws Exception {
            facade.register("testUser", "password123", "test@email.com");

            facade.logout();

            Assertions.assertThrows(UnauthorizedException.class, () ->
                    facade.logout()
            );
        }

        @Test
        @DisplayName("Logging out with the wrong token")
        public void logoutInvalidToken() throws Exception {
            facade.register("testUser", "password123", "test@email.com");
        }
    }

    @Nested
    class ClearTests {
        @Test
        @DisplayName("Successful clearing of the database")
        public void clearActuallyClearsDatabase() throws Exception {
            facade.register("testUser", "password123", "test@email.com");

            facade.clear();

            Assertions.assertThrows(UnauthorizedException.class, () ->
                    facade.login("testUser", "password123")
            );
        }
    }

    @Nested
    class CreateGameTests {
        @Test
        @DisplayName("Successful creation of a game")
        public void createGameSuccess() throws Exception {
            facade.register("testUser", "password123", "test@email.com");

            Assertions.assertEquals(
                    1,
                    facade.createGame("My Game"));
        }

        @Test
        @DisplayName("Creating game without being logged in")
        public void createGameUnauthorized() {
            Assertions.assertThrows(
                    UnauthorizedException.class,
                    () -> facade.createGame("My Game")
            );
        }
    }

    @Nested
    class ListGameTests {
        @Test
        @DisplayName("Successful listing of games")
        public void listGamesSuccess() throws Exception {
            facade.register("testUser", "password123", "test@email.com");

            facade.createGame("Game 1");
            facade.createGame("Game 2");

            List<Game> games = facade.listGames();

            Assertions.assertEquals(2, games.size());
            Assertions.assertEquals("Game 1", games.get(0).gameName());
            Assertions.assertEquals("Game 2", games.get(1).gameName());
        }

        @Test
        @DisplayName("There are no games")
        public void listGamesEmpty() throws Exception {
            facade.register("testUser", "password123", "test@email.com");

            List<Game> games = facade.listGames();

            Assertions.assertNotNull(games);
            Assertions.assertTrue(games.isEmpty());
        }

        @Test
        @DisplayName("Requesting list without being logged in")
        public void listGamesUnauthorized() {
            Assertions.assertThrows(
                    UnauthorizedException.class,
                    () -> facade.listGames()
            );
        }

        @Test
        @DisplayName("List games returns the right data")
        public void listGamesContainsCorrectGameData() throws Exception {
            facade.register("testUser", "password123", "test@email.com");

            facade.createGame("My Game");

            List<Game> games = facade.listGames();

            Assertions.assertEquals(1, games.size());

            Game game = games.getFirst();

            Assertions.assertEquals("My Game", game.gameName());
            Assertions.assertNull(game.whiteUsername());
            Assertions.assertNull(game.blackUsername());
            Assertions.assertTrue(game.gameID() > 0);
        }
    }

    @Nested
    class JoinGameTests {
        @Test
        @DisplayName("Successfully joining a game")
        public void joinGameSuccess() throws Exception {
            facade.register("testUser", "password123", "test@email.com");
            facade.createGame("My Game");

            List<Game> games = facade.listGames();
            int gameID = games.getFirst().gameID();

            Assertions.assertDoesNotThrow(() -> facade.joinGame(ChessGame.TeamColor.WHITE, gameID));
        }

        @Test
        @DisplayName("Join game without being logged in")
        public void joinGameUnauthorized() {
            Assertions.assertThrows(
                    UnauthorizedException.class,
                    () -> facade.joinGame(ChessGame.TeamColor.WHITE, 1)
            );
        }

        @Test
        @DisplayName("Join a game that has a white player as white")
        public void joinGameAlreadyTaken() throws Exception {
            facade.register("testUser", "password123", "test@email.com");
            facade.createGame("My Game");

            int gameID = facade.listGames().getFirst().gameID();

            facade.joinGame(ChessGame.TeamColor.WHITE, gameID);

            Assertions.assertThrows(
                    AlreadyTakenException.class,
                    () -> facade.joinGame(ChessGame.TeamColor.WHITE, gameID)
            );
        }

        @Test
        @DisplayName("Join game twice with different colors")
        public void joinGameDifferentColors() throws Exception {
            facade.register("testUser", "password123", "test@email.com");
            facade.createGame("My Game");

            int gameID = facade.listGames().getFirst().gameID();

            facade.joinGame(ChessGame.TeamColor.WHITE, gameID);

            Assertions.assertDoesNotThrow(
                    () -> facade.joinGame(ChessGame.TeamColor.BLACK, gameID)
            );
        }

        @Test
        @DisplayName("Join a game with incorrect game ID")
        public void joinGameNonexistentGame() throws Exception {
            facade.register("testUser", "password123", "test@email.com");

            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> facade.joinGame(ChessGame.TeamColor.WHITE, -1)
            );
        }
    }
}
