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
    RegisterRequest johnDoeReq = new RegisterRequest(username, password, email);

    @BeforeEach
    void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();

        myService = new UserService(userDAO, authDAO);
    }

    @Nested
    class RegistrationTests {

        @Test
        @DisplayName("No username, password or email")
        void missingArgument() {
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.register(new RegisterRequest(username, password, null)));
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.register(new RegisterRequest(username, null, email)));
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.register(new RegisterRequest(null, password, email)));
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
            Assertions.assertInstanceOf(RegisterResult.class, res);
            Assertions.assertInstanceOf(String.class, res.authToken());
            Assertions.assertEquals(username, res.username());
        }
    }

    @Nested
    class LoginTests {

        @Test
        @DisplayName("No username or no password")
        void missingArgument() throws DataAccessException, AlreadyTakenException {
            myService.register(johnDoeReq);
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.login(new LoginRequest(null, password)));
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.login(new LoginRequest(username, null)));
        }

        @Test
        @DisplayName("Wrong username")
        void wrongUsername() throws DataAccessException, AlreadyTakenException {
            myService.register(johnDoeReq);
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.login(new LoginRequest("JohnDough", password)));
        }

        @Test
        @DisplayName("Wrong password")
        void wrongPassword() throws DataAccessException, AlreadyTakenException {
            myService.register(johnDoeReq);
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.login(new LoginRequest(username, "password")));
        }

        @Test
        @DisplayName("Logging in")
        void loggingIn() throws DataAccessException, AlreadyTakenException, UnauthorizedException {
            myService.register(johnDoeReq);
            LoginResult res = myService.login(new LoginRequest(username, password));
            Assertions.assertInstanceOf(LoginResult.class, res);
            Assertions.assertInstanceOf(String.class, res.authToken());
            Assertions.assertEquals(username, res.username());
        }
    }

    @Nested
    class LogoutTests {

        @Test
        @DisplayName("Wrong authToken")
        void wrongAuthToken() throws DataAccessException, AlreadyTakenException, UnauthorizedException {
            myService.register(johnDoeReq);
            myService.login(new LoginRequest(username, password));
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.logout("Behold my auth token"));
        }

        @Test
        @DisplayName("Logging out")
        void loggingOut() throws DataAccessException, AlreadyTakenException, UnauthorizedException {
            myService.register(johnDoeReq);
            LoginResult loginRes = myService.login(new LoginRequest(username, password));
            String authToken = loginRes.authToken();
            myService.logout(authToken);
            Assertions.assertNull(authDAO.get(authToken));
        }
    }
}
