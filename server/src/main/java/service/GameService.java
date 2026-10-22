package service;

import dataaccess.GameDAO;

public class GameService {

    GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public void clearDB() {
        gameDAO.clear();
    }
}
