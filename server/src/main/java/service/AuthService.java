package service;

import dataaccess.AuthDAO;

public class AuthService {

    AuthDAO authDao;

    public AuthService(AuthDAO authDAO) {
        this.authDao = authDAO;
    }

    public void clearDB() {
        authDao.clear();
    }
}
