package service;

import dataaccess.*;
import model.Auth;
import model.Game;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;

public class GameServiceTests {

    GameDAO gameDAO;
    AuthDAO authDAO;

    GameService myService;

    @BeforeEach
    void setUp() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();

        myService = new GameService(gameDAO, authDAO);
    }

    @Nested
    class listGameTests {

        @Test
        @DisplayName("Wrong authentification token")
        void wrongAuthToken() {
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.listGame("Behold my auth token"));
        }

        @Test
        @DisplayName("Requesting the games")
        void requestGames() throws DataAccessException, UnauthorizedException {
            String myAuthToken = "myAuthToken";
            authDAO.add(new Auth(myAuthToken, "John Doe"));
            String gameName = "John's game";
            gameDAO.add(gameName);
            Game first = myService.listGame(myAuthToken).getFirst();
            Assertions.assertSame(
                    gameName,
                    first.gameName());
            Assertions.assertNull(first.game());
        }
    }

    @Nested
    class CreateGameTests {

        String authToken = "AT";
        String gameName = "John's game";
        String username = "John Doe";
        CreateGameRequest gameReq = new CreateGameRequest(gameName);

        @Test
        @DisplayName("Wrong authentification token")
        void wrongAuthToken() {
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.createGame("Behold my auth token", gameReq));
        }

        @Test
        @DisplayName("No game name")
        void misingArgument() throws DataAccessException {
            authDAO.add(new Auth(authToken, username));
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.createGame(authToken, new CreateGameRequest(null)));
        }

        @Test
        @DisplayName("Adding a game")
        void addGame() throws DataAccessException {
            authDAO.add(new Auth(authToken, username));
            Assertions.assertDoesNotThrow(
                    () -> myService.createGame(authToken, gameReq));
        }
    }
}
