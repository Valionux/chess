package service;

import chess.ChessGame;
import dataaccess.*;
import model.Auth;
import model.Game;
import org.junit.jupiter.api.*;
import request.CreateGameRequest;
import request.JoinGameRequest;

public class GameServiceTests {

    GameDAO gameDAO;
    AuthDAO authDAO;

    GameService myService;

    @BeforeEach
    void setUp() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();

        myService = new GameService(gameDAO, authDAO);
    }

    @Nested
    class ListGameTests {

        @Test
        @DisplayName("Wrong authentification token")
        void wrongAuthToken() {
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.listGame("Behold my auth token"));
        }

        @Test
        @DisplayName("Requesting the games")
        void requestGames() throws DataAccessException, UnauthorizedException {
            String myAuthToken = "myAuthToken";
            authDAO.add(new Auth(myAuthToken, "John Doe"));
            String gameName = "John's game";
            gameDAO.add(gameName);
            Game first = myService.listGame(myAuthToken).getFirst();
            Assertions.assertSame(
                    gameName,
                    first.gameName());
            Assertions.assertNull(first.game());
        }
    }

    @Nested
    class CreateGameTests {

        String authToken = "AT";
        String gameName = "John's game";
        String username = "John Doe";
        CreateGameRequest gameReq = new CreateGameRequest(gameName);

        @Test
        @DisplayName("Wrong authentification token")
        void wrongAuthToken() {
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.createGame("Behold my auth token", gameReq));
        }

        @Test
        @DisplayName("No game name")
        void misingArgument() throws DataAccessException {
            authDAO.add(new Auth(authToken, username));
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.createGame(authToken, new CreateGameRequest(null)));
        }

        @Test
        @DisplayName("Adding a game")
        void addGame() throws DataAccessException {
            authDAO.add(new Auth(authToken, username));
            Assertions.assertDoesNotThrow(
                    () -> myService.createGame(authToken, gameReq));
        }
    }

    @Nested
    class JoinGameTests {

        String authToken = "AT";
        int wrongGameId = 31;
        String username = "John Doe";
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;
        JoinGameRequest joinGameReq = new JoinGameRequest(white, wrongGameId);

        @Test
        @DisplayName("Wrong authentification token")
        void wrongAuthToken() {
            Assertions.assertThrows(UnauthorizedException.class,
                    () -> myService.joinGame("Behold my auth token", joinGameReq));
        }

        @Test
        @DisplayName("No player color or wrong game ID")
        void misingArgument() throws DataAccessException {
            authDAO.add(new Auth(authToken, username));
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.joinGame(authToken, new JoinGameRequest(null, wrongGameId)));
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> myService.joinGame(authToken, new JoinGameRequest(white, wrongGameId)));
        }

        @Test
        @DisplayName("Add a player when there are no seats")
        void takenSeat() throws DataAccessException {
            authDAO.add(new Auth(authToken, username));
            int gameID = gameDAO.add("John's game");
            gameDAO.addUserToGame(gameID, username, white);
            gameDAO.addUserToGame(gameID, username, black);
            Assertions.assertThrows(AlreadyTakenException.class,
                    () -> myService.joinGame(authToken, new JoinGameRequest(white, gameID)));
        }

        @Test
        @DisplayName("Add a player to white")
        void addWhite() throws DataAccessException {
            authDAO.add(new Auth(authToken, username));
            int gameID = gameDAO.add("John's game");
            gameDAO.addUserToGame(gameID, username, white);
            Assertions.assertEquals(
                    username,
                    gameDAO.get(gameID).whiteUsername());
        }

        @Test
        @DisplayName("Add a player to black")
        void addBlack() throws DataAccessException {
            authDAO.add(new Auth(authToken, username));
            int gameID = gameDAO.add("John's game");
            gameDAO.addUserToGame(gameID, username, black);
            Assertions.assertEquals(
                    username,
                    gameDAO.get(gameID).blackUsername());
        }
    }
}
