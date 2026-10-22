package dataaccess;

import model.User;

public interface UserDAO {
    void clear() throws DataAccessException;

    void add(User user) throws DataAccessException;

    User get(String username) throws DataAccessException;
}
