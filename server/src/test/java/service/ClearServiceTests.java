package service;

import dataaccess.*;
import model.Auth;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClearServiceTests {

    AuthDAO authDAO;
    GameDAO gameDAO;
    UserDAO userDAO;

    ClearService myService;

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();

        myService = new ClearService(authDAO, userDAO, gameDAO);
    }

    @Test
    @DisplayName("Clear non empty AuthDAO")
    public void clearAuthDAO() throws DataAccessException {
        String authToken = "that'sACoolAuthTokenJohn";
        String username = "John Doe";
        String gameName = "Doe's";
        Auth auth = new Auth(authToken, username);
        User user = new User(username, "1234", "john.doe@byu.edu");
        authDAO.add(auth);
        userDAO.add(user);
        gameDAO.add(gameName);
        myService.clearDB();
        Assertions.assertNull(authDAO.get(authToken));
        Assertions.assertNull(userDAO.get(username));
        Assertions.assertThrows(
                DataAccessException.class,
                () -> gameDAO.get(0));
    }
}
