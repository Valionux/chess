package dataaccess;

import chess.ChessGame;
import model.Game;

import java.util.List;

public interface GameDAO {
    void clear() throws DataAccessException;

    void add(String gameName) throws DataAccessException;

    Game get(int gameID) throws DataAccessException;

    List<Game> listGames() throws DataAccessException;

    void addUserToGame(int gameID, String username, ChessGame.TeamColor teamColor) throws DataAccessException;
}
