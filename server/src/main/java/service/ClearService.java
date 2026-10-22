package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {

    AuthDAO authDao;
    UserDAO userDAO;
    GameDAO gameDAO;

    public ClearService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDao = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void clearDB() throws DataAccessException {
        authDao.clear();
        userDAO.clear();
        gameDAO.clear();
    }
}
