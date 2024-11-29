```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class DBConnectionProviderTest {

    @Container
    public PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private DBConnectionProvider dbConnectionProvider;

    @BeforeEach
    public void setUp() {
        String url = "jdbc:postgresql://" + postgresqlContainer.getContainerIpAddress() + ":" 
                     + postgresqlContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT) 
                     + "/test?user=test&password=test";
        dbConnectionProvider = new DBConnectionProvider(url, "test", "test");
    }

    @AfterEach
    public void tearDown() {
        postgresqlContainer.stop();
    }

    @Test
    public void testGetConnection() throws SQLException {
        Connection connection = dbConnectionProvider.getConnection();
        assertNotNull(connection);
    }
}
```