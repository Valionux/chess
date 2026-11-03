package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.Auth;
import model.Game;
import org.junit.jupiter.api.*;

public class SQLGameDAOTests {

    SQLGameDAO DAO = new SQLGameDAO();

    public SQLGameDAOTests() throws DataAccessException {
    }

    @BeforeAll
    static void setUp() {
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        DAO.clear();
    }

    Game game = new Game(1, null, null, "game", new ChessGame());

    @Nested
    class ClearTests {

        @Test
        @DisplayName("Adding Games and clearing them")
        void addAndClear() throws DataAccessException {
            var id1 = DAO.add("game");
            var id2 = DAO.add("game1");
            DAO.clear();
            Assertions.assertNull(DAO.get(id1));
            Assertions.assertNull(DAO.get(id2));
        }
    }

    @Nested
    class AddTests {

        @Test
        @DisplayName("Adding a Game")
        void addGame() throws DataAccessException {
            var id = DAO.add("game");
            var resGame = DAO.get(id);
            Assertions.assertEquals(game,
                    resGame);
        }

        @Test
        @DisplayName("Adding a wrong Game")
        void addWrongAuth() {
            Assertions.assertThrows(DataAccessException.class,
                    () -> DAO.add(null));
        }
    }

    @Nested
    class AddUserTests {

        @Test
        @DisplayName("Adding a user to a game")
        void addUser() throws DataAccessException {
            var id = DAO.add("game");
            DAO.addUserToGame(id, "John", ChessGame.TeamColor.WHITE);
            Assertions.assertEquals("John", DAO.get(id).whiteUsername());
        }
    }
}
