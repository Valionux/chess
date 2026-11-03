package dataaccess.sql;

import dataaccess.DataAccessException;
import model.Auth;
import org.junit.jupiter.api.*;

public class SQLAuthDAOTests {

    SQLAuthDAO authDAO = new SQLAuthDAO();

    public SQLAuthDAOTests() throws DataAccessException {
    }

    @BeforeAll
    static void setUp() {
    }

    @AfterEach
    void cleanUp() throws DataAccessException {
        authDAO.clear();
    }

    Auth auth = new Auth("bogus1", "John1");

    @Nested
    class ClearTests {

        @Test
        @DisplayName("Adding Auths and deleting them")
        void addAndClear() throws DataAccessException {
            authDAO.add(auth);
            authDAO.add(new Auth("bogus2", "John2"));
            authDAO.clear();
            Assertions.assertNull(authDAO.get("bogus1"));
            Assertions.assertNull(authDAO.get("bogus2"));
        }
    }

    @Nested
    class AddTests {

        @Test
        @DisplayName("Adding an Auth")
        void addAuth() throws DataAccessException {
            authDAO.add(auth);
            Auth bogus1 = authDAO.get("bogus1");
            Assertions.assertEquals(auth,
                    new Auth(bogus1.authToken(), bogus1.username()));
        }

        @Test
        @DisplayName("Adding a wrong Auth")
        void addWrongAuth() {
            Assertions.assertThrows(DataAccessException.class,
                    () -> authDAO.add(new Auth("bogus1", null)));
        }
    }

    @Nested
    class DeleteTests {
        @Test
        @DisplayName("Adding and deleting an Auth")
        void addAuth() throws DataAccessException {
            authDAO.add(auth);
            authDAO.delete(auth.authToken());
            Assertions.assertNull(authDAO.get(auth.authToken()));
        }
    }
}
