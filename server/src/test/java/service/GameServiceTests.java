package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;

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
}
