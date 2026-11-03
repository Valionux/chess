package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.User;

import java.sql.Connection;

import static dataaccess.sql.DAOUtils.configureDatabase;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        configureDatabase(createStatements);
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE user";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not clear database", e);
        }
    }

    @Override
    public void add(User user) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement =
                         conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("Could not add user record", e);
        }
    }

    @Override
    public User get(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password, email " +
                    "FROM user " +
                    "WHERE username = ?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var resUsername = rs.getString("username");
                        var resPassword = rs.getString("password");
                        var resEmail = rs.getString("email");

                        return new User(resUsername, resPassword, resEmail);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException("could not get auth record", e);
        }
        return null;
    }

}
