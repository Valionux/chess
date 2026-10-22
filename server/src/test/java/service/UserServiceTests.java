package service;

import dataaccess.*;
import org.junit.jupiter.api.BeforeEach;

public class UserServiceTests {

    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    UserService myService = new UserService(userDAO, authDAO);

    @BeforeEach
    void setUp() throws DataAccessException {
        new ClearService(authDAO, userDAO, new MemoryGameDAO()).clearDB();
    }
}
