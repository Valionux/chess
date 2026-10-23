package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.Auth;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import response.LoginResult;
import response.RegisterResult;

import java.util.Objects;
import java.util.UUID;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest req) throws IllegalArgumentException, DataAccessException, AlreadyTakenException {
        String email = req.email();
        String password = req.password();
        String username = req.username();
        if (email == null
                || password == null
                || username == null) {
            throw new IllegalArgumentException("A username, a password and an email address must be provided");
        }

        if (userDAO.get(username) != null) {
            throw new AlreadyTakenException("This username is already taken");
        }

        userDAO.add(new User(username, password, email));

        String authToken = startSession(username);

        return new RegisterResult(username, authToken);
    }

    public LoginResult login(LoginRequest req) throws DataAccessException, UnauthorizedException {
        String password = req.password();
        String username = req.username();
        if (password == null
                || username == null) {
            throw new IllegalArgumentException("A username, a password and an email address must be provided");
        }

        var user = userDAO.get(username);

        if (user == null
                || !Objects.equals(user.password(), password)) {
            throw new UnauthorizedException("Username or password incorrect");
        }

        String authToken = startSession(username);

        return new LoginResult(username, authToken);
    }

    public void logout(String authToken) throws DataAccessException, UnauthorizedException {
        if (authDAO.delete(authToken) == null) {
            throw new UnauthorizedException("Wrong authentification token");
        }
    }

    private String startSession(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        authDAO.add(new Auth(authToken, username));
        return authToken;
    }
}
