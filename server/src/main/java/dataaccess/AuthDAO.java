package dataaccess;

import model.Auth;

public interface AuthDAO {
    void clear() throws DataAccessException;

    void add(Auth auth) throws DataAccessException;

    Auth get(String authToken) throws DataAccessException;

    void delete(String authToken) throws DataAccessException;
}
