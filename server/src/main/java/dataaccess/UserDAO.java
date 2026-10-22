package dataaccess;

import model.User;

public interface UserDAO {
    void clear();

    void add(User user);

    User get(String username);
}
