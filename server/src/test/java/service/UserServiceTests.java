package service;

import dataaccess.*;
import model.User;
import org.junit.jupiter.api.*;
import request.RegisterRequest;
import response.RegisterResult;

public class UserServiceTests {

    UserDAO userDAO;
    AuthDAO authDAO;

    UserService myService;

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();

        myService = new UserService(userDAO, authDAO);
    }

    @Nested
    class RegistrationTests {

        final String username = "John Doe";
        final String password = "1234";
        final String email = "john.doe@byu.edu";
        final User johnDoe = new User(username, password, email);
        final RegisterRequest johnDoeReq = new RegisterRequest(username, password, email);


        @Test
        @DisplayName("Wrong email")
        void wrongEmail() {
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.register(new RegisterRequest(username, password, null)));
        }

        @Test
        @DisplayName("Taken username")
        void takenUsername() throws DataAccessException {
            userDAO.add(johnDoe);
            Assertions.assertThrows(AlreadyTakenException.class,
                    () -> myService.register(johnDoeReq));
        }

        @Test
        @DisplayName("Adding a user")
        void addUser() throws DataAccessException, AlreadyTakenException {
            RegisterResult res = myService.register(johnDoeReq);
            Assertions.assertNotNull(res);
            Assertions.assertNotNull(res.authToken());
            Assertions.assertNotNull(res.username());
        }
    }
}
