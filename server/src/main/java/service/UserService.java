package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.Auth;
import model.User;
import request.RegisterRequest;
import response.RegisterResult;

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

        String authToken = UUID.randomUUID().toString();
        authDAO.add(new Auth(authToken, username));

        var model = authDAO.get(authToken);

        return new RegisterResult(model.authToken(), model.username());
    }
}
