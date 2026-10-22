package service;

import chess.ChessGame;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameServiceTests {

    GameDAO myDAO = new MemoryGameDAO();

    GameService myService = new GameService(myDAO);

    @BeforeEach
    @Test
    @DisplayName("Clear non empty GameDAO")
    public void clearGameDAO() {
        String gameName = "Doe's";
        myDAO.add(gameName);
        myService.clearDB();
        Assertions.assertThrows(
                IndexOutOfBoundsException.class,
                () -> myDAO.get(0));
    }
}
