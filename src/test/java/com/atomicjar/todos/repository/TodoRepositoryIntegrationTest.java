```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
public class TodoRepositoryTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
            .withUsername("test")
            .withPassword("test")
            .withDatabaseName("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testGetPendingTodos() {
        List<Todo> todos = todoRepository.getPendingTodos();
        assertNotNull(todos);
    }

    @Test
    public void testGetCompletedTodos() {
        List<Todo> todos = todoRepository.getCompletedTodos();
        assertNotNull(todos);
    }
}
```