package service;

import dataaccess.*;
import model.Auth;
import model.Game;
import org.junit.jupiter.api.*;

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
}
