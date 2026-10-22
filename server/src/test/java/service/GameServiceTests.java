package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;

public class GameServiceTests {

    GameDAO gameDAO = new MemoryGameDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    GameService myService = new GameService(gameDAO, authDAO);

    @BeforeEach
    void setUp() throws DataAccessException {
        new ClearService(authDAO, new MemoryUserDAO(), gameDAO).clearDB();
    }

}
