package service;

import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserServiceTests {

    UserDAO myDAO = new MemoryUserDAO();

    UserService myService = new UserService(myDAO);

    @BeforeEach
    @Test
    @DisplayName("Clear non empty UserDAO")
    public void clearAuthDAO() {
        String username = "John Doe";
        User user = new User(username, "1234", "john.doe@byu.edu");
        myDAO.add(user);
        myService.clearDB();
        Assertions.assertNull(myDAO.get(username));
    }
}
