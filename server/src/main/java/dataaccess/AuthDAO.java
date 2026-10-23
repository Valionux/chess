package dataaccess;

import model.Auth;
import service.UnauthorizedException;

public interface AuthDAO {
    void clear() throws DataAccessException;

    void add(Auth auth) throws DataAccessException;

    Auth get(String authToken) throws DataAccessException;

    Auth delete(String authToken) throws DataAccessException, UnauthorizedException;
}
