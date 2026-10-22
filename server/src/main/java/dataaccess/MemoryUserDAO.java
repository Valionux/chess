package dataaccess;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private Map<String, User> userData = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        try {
            userData.clear();
        } catch (Exception e) {
            throw new DataAccessException("Failed to clear user records", e);
        }
    }

    @Override
    public void add(User user) throws DataAccessException {
        try {
            userData.put(user.username(), user);
        } catch (Exception e) {
            throw new DataAccessException("Failed to insert user record", e);
        }
    }

    @Override
    public User get(String username) throws DataAccessException {
        try {
            return userData.get(username);
        } catch (Exception e) {
            throw new DataAccessException("Failed to retrieve user record", e);
        }
    }
}
