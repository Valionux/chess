package dataaccess.sql;

import dataaccess.DataAccessException;
import model.User;
import org.junit.jupiter.api.*;

public class SQLUserDAOTests {

    SQLUserDAO userDAO = new SQLUserDAO();

    public SQLUserDAOTests() throws DataAccessException {
    }

    @BeforeAll
    static void setUp() {
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        userDAO.clear();
    }

    User user = new User("John1", "1234", "y@gmail.com");

    @Nested
    class ClearTests {

        @Test
        @DisplayName("Adding Users and clearing them")
        void addAndClear() throws DataAccessException {
            userDAO.add(user);
            userDAO.add(new User("bogus2", "John2", "email@boof"));
            userDAO.clear();
            Assertions.assertNull(userDAO.get("John1"));
            Assertions.assertNull(userDAO.get("bogus2"));
        }
    }

    @Nested
    class AddTests {

        @Test
        @DisplayName("Adding a User")
        void addAuth() throws DataAccessException {
            userDAO.add(user);
            User john1 = userDAO.get("john1");
            Assertions.assertEquals(user,
                    new User(john1.username(), john1.password(), john1.email()));
        }

        @Test
        @DisplayName("Adding a wrong User")
        void addWrongUser() {
            Assertions.assertThrows(DataAccessException.class,
                    () -> userDAO.add(new User("bogus1", null, null)));
        }
    }
}
