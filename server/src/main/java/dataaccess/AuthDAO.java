package dataaccess;

import model.Auth;

public interface AuthDAO {
    void clear();

    void creatAuth(Auth auth);

    Auth getAuth(String authToken);

    void deleteAuth(String authToken);
}
