package service;

import dataaccess.*;
import model.User;
import org.junit.jupiter.api.*;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResult;
import response.RegisterResult;

public class UserServiceTests {

    UserDAO userDAO;
    AuthDAO authDAO;

    UserService myService;

    final String username = "John Doe";
    final String password = "1234";
    final String email = "john.doe@byu.edu";
    final User johnDoe = new User(username, password, email);

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();

        myService = new UserService(userDAO, authDAO);
    }

    @Nested
    class RegistrationTests {

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

    @Nested
    class LoginTests {

        @Test
        @DisplayName("Wrong username")
        void wrongUsername() throws DataAccessException, AlreadyTakenException {
            myService.register(new RegisterRequest(username, password, email));
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.login(new LoginRequest("JohnDough", password)));
        }

        @Test
        @DisplayName("Wrong password")
        void wrongPassword() throws DataAccessException, AlreadyTakenException {
            myService.register(new RegisterRequest(username, password, email));
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.login(new LoginRequest(username, "password")));
        }

        @Test
        @DisplayName("Logging in")
        void loggingIn() throws DataAccessException, AlreadyTakenException, UnauthorizedException {
            myService.register(new RegisterRequest(username, password, email));
            LoginResult res = myService.login(new LoginRequest(username, password));
            Assertions.assertNotNull(res);
            Assertions.assertNotNull(res.authToken());
            Assertions.assertNotNull(res.username());
        }
    }
}
