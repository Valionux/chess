package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import model.Auth;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AuthServiceTests {

    AuthDAO myDAO = new MemoryAuthDAO();

    AuthService myService = new AuthService(myDAO);

    @BeforeEach
    @Test
    @DisplayName("Clear non empty AuthDAO")
    public void clearAuthDAO() {
        String authToken = "that'sACoolAuthTokenJohn";
        Auth auth = new Auth(authToken, "John Doe");
        myDAO.add(auth);
        myService.clearDB();
        Assertions.assertNull(myDAO.get(authToken));
    }
}
