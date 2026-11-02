package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.Auth;
import model.User;
import org.junit.jupiter.api.*;

public class SQLUserDAOTests {

    SQLUserDAO DAO = new SQLUserDAO();

    public SQLUserDAOTests() throws DataAccessException {
    }

    @BeforeAll
    static void setUp() {
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        DAO.clear();
    }

    User user = new User("John1", "1234", "y@gmail.com");

    @Nested
    class ClearTests {

        @Test
        @DisplayName("Adding Users and clearing them")
        void addAndClear() throws DataAccessException {
            DAO.add(user);
            DAO.add(new User("bogus2", "John2", "email@boof"));
            DAO.clear();
            Assertions.assertNull(DAO.get("John1"));
            Assertions.assertNull(DAO.get("bogus2"));
        }
    }

    @Nested
    class AddTests {

        @Test
        @DisplayName("Adding a User")
        void addAuth() throws DataAccessException {
            DAO.add(user);
            User John1 = DAO.get("John1");
            Assertions.assertEquals(user,
                    new User(John1.username(), John1.password(), John1.email()));
        }

        @Test
        @DisplayName("Adding a wrong User")
        void addWrongUser() {
            Assertions.assertThrows(DataAccessException.class,
                    () -> DAO.add(new User("bogus1", null, null)));
        }
    }
}
