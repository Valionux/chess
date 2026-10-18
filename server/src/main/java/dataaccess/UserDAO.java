package dataaccess;

import model.User;

public interface UserDAO {
    void clear();

    void createUser(User user);

    User getUser(String username);
}
