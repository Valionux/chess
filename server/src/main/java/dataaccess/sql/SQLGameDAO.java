package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.Game;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE game";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not clear game records", e);
        }
    }

    @Override
    public int add(String gameName) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement =
                         conn.prepareStatement("INSERT INTO game " +
                                         "(whiteUsername, blackUsername, gameName, chessGame) " +
                                         "VALUES(NULL, NULL, ?, ?)",
                                 Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, new Gson().toJson(new ChessGame()));

                preparedStatement.executeUpdate();

                var resultSet = preparedStatement.getGeneratedKeys();
                var id = 0;
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }

                return id;
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not add game record", e);
        }
    }

    @Override
    public Game get(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT id, whiteUsername, blackUsername, gameName, chessGame " +
                            "FROM game " +
                            "WHERE id = ?")) {
                preparedStatement.setInt(1, gameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var resId = rs.getInt("id");
                        var resWhiteUsername = rs.getString("whiteUsername");
                        var resBlackUsername = rs.getString("blackUsername");
                        var resGameName = rs.getString("gameName");
                        var resChessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);

                        return new Game(resId, resWhiteUsername, resBlackUsername, resGameName, resChessGame);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve game record", e);
        }
        return null;
    }

    @Override
    public List<Game> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT id, whiteUsername, blackUsername, gameName, chessGame " +
                            "FROM game ")) {
                try (var rs = preparedStatement.executeQuery()) {
                    var result = new ArrayList<Game>();
                    while (rs.next()) {
                        var resId = rs.getInt("id");
                        var resWhiteUsername = rs.getString("whiteUsername");
                        var resBlackUsername = rs.getString("blackUsername");
                        var resGameName = rs.getString("gameName");
                        var resChessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);

                        result.add(new Game(resId, resWhiteUsername, resBlackUsername, resGameName, resChessGame));
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("could not retrieve game records", e);
        }
    }

    @Override
    public void addUserToGame(int gameID, String username, ChessGame.TeamColor teamColor) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {

            var columnName = teamColor == ChessGame.TeamColor.WHITE ? "whiteUsername" : "blackUsername";

            try (var preparedStatement =
                         conn.prepareStatement("UPDATE game SET " + columnName + "=?" +
                                 "WHERE id=?")) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("could not add user to the game", e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `id` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `chessGame` longtext NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(id)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci AUTO_INCREMENT = 1
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("could not configure the database");
        }
    }
}
