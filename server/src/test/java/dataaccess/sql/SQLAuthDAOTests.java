package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.Auth;
import org.junit.jupiter.api.*;

public class SQLAuthDAOTests {

    SQLAuthDAO DAO = new SQLAuthDAO();

    public SQLAuthDAOTests() throws DataAccessException {
    }

    @BeforeAll
    static void setUp() {
        DatabaseManager.useTestDatabase();
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        DAO.clear();
    }

    Auth auth = new Auth("bogus1", "John1");

    @Nested
    class ClearTests {

        @Test
        @DisplayName("Adding Auths and deleting them")
        void addAndClear() throws DataAccessException {
            DAO.add(auth);
            DAO.add(new Auth("bogus2", "John2"));
            DAO.clear();
            Assertions.assertThrows(DataAccessException.class,
                    () -> DAO.get("bogus1"));
            Assertions.assertThrows(DataAccessException.class,
                    () -> DAO.get("bogus2"));
        }
    }

    @Nested
    class AddTests {

        @Test
        @DisplayName("Adding an Auth")
        void addAuth() throws DataAccessException {
            DAO.add(auth);
            Auth bogus1 = DAO.get("bogus1");
            Assertions.assertEquals(auth,
                    new Auth(bogus1.authToken(), bogus1.username()));
        }

        @Test
        @DisplayName("Adding a wrong Auth")
        void addWrongAuth() {
            Assertions.assertThrows(DataAccessException.class,
                    () -> DAO.add(new Auth("bogus1", null)));
        }
    }

    @Nested
    class GetTests {

    }

    @Nested
    class DeleteTests {

    }
}
