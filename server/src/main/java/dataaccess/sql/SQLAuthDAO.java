package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.Auth;
import service.UnauthorizedException;

import java.sql.Connection;
import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {

    SQLAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE auth";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not clear database", e);
        }
    }

    @Override
    public void add(Auth auth) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement =
                         conn.prepareStatement("INSERT INTO auth (authToken, username) VALUES(?, ?)")) {
                preparedStatement.setString(1, auth.authToken());
                preparedStatement.setString(2, auth.username());

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not clear database", e);
        }
    }

    @Override
    public Auth get(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public Auth delete(String authToken) throws DataAccessException, UnauthorizedException {
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
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
