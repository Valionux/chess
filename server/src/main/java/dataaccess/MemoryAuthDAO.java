package dataaccess;

import model.Auth;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private Map<String, Auth> authData = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        try {
            authData.clear();
        } catch (Exception e) {
            throw new DataAccessException("Failed to clear authentification records", e);
        }
    }

    @Override
    public void add(Auth auth) throws DataAccessException {
        try {
            authData.put(auth.authToken(), auth);
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert authentification record", e);
        }
    }

    @Override
    public Auth get(String authToken) throws DataAccessException {
        try {
            return authData.get(authToken);
        } catch (Exception e) {
            throw new DataAccessException("Failed to retrieve authentification record", e);
        }
    }

    @Override
    public void delete(String authToken) throws DataAccessException {
        try {
            authData.remove(authToken);
        } catch (Exception e) {
            throw new DataAccessException("Failed to delete authentification record", e);
        }
    }


}
