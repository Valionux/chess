package dataaccess;

import model.Auth;

public interface AuthDAO {
    void clear();

    void add(Auth auth);

    Auth get(String authToken);

    void delete(String authToken);
}
