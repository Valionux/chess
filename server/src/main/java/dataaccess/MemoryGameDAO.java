package dataaccess;

import chess.ChessGame;
import model.Game;

import java.util.*;

public class MemoryGameDAO implements GameDAO {

    private final List<Game> gameData = new ArrayList<>();

    @Override
    public void clear() throws DataAccessException {
        try {
            gameData.clear();
        } catch (Exception e) {
            throw new DataAccessException("Failed to clear game records", e);
        }
    }

    @Override
    public void add(String gameName) throws DataAccessException {
        try {
            gameData.add(new Game(gameData.size(), null, null, gameName, new ChessGame()));
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert game record", e);
        }
    }

    @Override
    public Game get(int gameID) throws DataAccessException {
        try {
            return gameData.get(gameID);
        } catch (Exception e) {
            throw new DataAccessException("Failed to retrieve game record", e);
        }
    }

    @Override
    public List<Game> listGames() throws DataAccessException {
        try {
            return gameData;
        } catch (Exception e) {
            throw new DataAccessException("Failed to retrieve game records", e);
        }
    }

    @Override
    public void addUserToGame(
            int gameID,
            String username,
            ChessGame.TeamColor teamColor)
            throws DataAccessException {
        try {
            System.out.println("Not implemented");
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert user into the game record", e);
        }
    }
}
