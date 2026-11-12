package client;

import org.junit.jupiter.api.*;
import server.Server;
import service.AlreadyTakenException;

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
}
