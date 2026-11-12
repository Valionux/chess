package dataaccess;

import exceptions.DataAccessException;
import model.Auth;
import exceptions.UnauthorizedException;

public interface AuthDAO {
    void clear() throws DataAccessException;

    void add(Auth auth) throws DataAccessException;

    Auth get(String authToken) throws DataAccessException;

    boolean delete(String authToken) throws DataAccessException, UnauthorizedException;
}
