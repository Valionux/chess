package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.Game;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

public class SQLGameDAOTests {

    SQLGameDAO gameDAO = new SQLGameDAO();

    public SQLGameDAOTests() throws DataAccessException {
    }

    @BeforeAll
    static void setUp() {
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        gameDAO.clear();
    }

    Game game = new Game(1, null, null, "game", new ChessGame());

    @Nested
    class ClearTests {

        @Test
        @DisplayName("Adding Games and clearing them")
        void addAndClear() throws DataAccessException {
            var id1 = gameDAO.add("game");
            var id2 = gameDAO.add("game1");
            gameDAO.clear();
            Assertions.assertNull(gameDAO.get(id1));
            Assertions.assertNull(gameDAO.get(id2));
        }
    }

    @Nested
    class AddTests {

        @Test
        @DisplayName("Adding a Game")
        void addGame() throws DataAccessException {
            var id = gameDAO.add("game");
            var resGame = gameDAO.get(id);
            Assertions.assertEquals(game,
                    resGame);
        }

        @Test
        @DisplayName("Adding a wrong Game")
        void addWrongAuth() {
            Assertions.assertThrows(DataAccessException.class,
                    () -> gameDAO.add(null));
        }
    }

    @Nested
    class AddUserTests {

        @Test
        @DisplayName("Adding a user to a game")
        void addUser() throws DataAccessException {
            var id = gameDAO.add("game");
            gameDAO.addUserToGame(id, "John", ChessGame.TeamColor.WHITE);
            Assertions.assertEquals("John", gameDAO.get(id).whiteUsername());
        }
    }

    @Nested
    class ListGameTests {

        @Test
        @DisplayName("Adding Games and retrieving them")
        void listGames() throws DataAccessException {
            gameDAO.add("game");
            gameDAO.add("game1");
            ArrayList<Game> games = new ArrayList<>(List.of(
                    game,
                    new Game(2, null, null, "game1", new ChessGame())));
            Assertions.assertEquals(games, gameDAO.listGames());
        }
    }
}
