```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private Todo pendingTodo;
    private Todo completedTodo;

    @BeforeEach
    public void setUp() {
        pendingTodo = new Todo();
        pendingTodo.setStatus(Todo.Status.PENDING);
        todoRepository.save(pendingTodo);

        completedTodo = new Todo();
        completedTodo.setStatus(Todo.Status.COMPLETED);
        todoRepository.save(completedTodo);
    }

    @AfterEach
    public void tearDown() {
        todoRepository.deleteAll();
    }

    @Test
    public void testGetPendingTodos() {
        List<Todo> pendingTodos = todoRepository.getPendingTodos();
        assertEquals(1, pendingTodos.size());
        assertEquals(pendingTodo, pendingTodos.get(0));
    }

    @Test
    public void testGetCompletedTodos() {
        List<Todo> completedTodos = todoRepository.getCompletedTodos();
        assertEquals(1, completedTodos.size());
        assertEquals(completedTodo, completedTodos.get(0));
    }
}
```