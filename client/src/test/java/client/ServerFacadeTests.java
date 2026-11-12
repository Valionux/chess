package client;

import org.junit.jupiter.api.*;
import server.Server;
import service.AlreadyTakenException;
import service.UnauthorizedException;

public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);

        facade = new HTTPServerFacade(
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
        public void registerSuccess() {
            Assertions.assertDoesNotThrow(() ->
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

            Assertions.assertDoesNotThrow(() ->
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

            Assertions.assertDoesNotThrow(() -> facade.createGame("My Game"));
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
}
