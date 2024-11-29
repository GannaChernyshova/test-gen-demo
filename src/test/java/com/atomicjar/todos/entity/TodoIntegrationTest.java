```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class TodoTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
        .withDatabaseName("test")
        .withUsername("test")
        .withPassword("test");

    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void testTodoCreation() {
        Todo todo = new Todo();
        todo.setId("1");
        todo.setTitle("Test Todo");
        todo.setCompleted(false);
        todo.setOrder(1);

        todoRepository.save(todo);

        Todo retrievedTodo = todoRepository.findById("1").orElse(null);
        assertNotNull(retrievedTodo);
        assertEquals("1", retrievedTodo.getId());
        assertEquals("Test Todo", retrievedTodo.getTitle());
        assertFalse(retrievedTodo.getCompleted());
        assertEquals(1, retrievedTodo.getOrder());
    }

    @Test
    public void testTodoUpdate() {
        Todo todo = todoRepository.findById("1").orElse(null);
        assertNotNull(todo);
        todo.setTitle("Updated Todo");
        todoRepository.save(todo);

        Todo updatedTodo = todoRepository.findById("1").orElse(null);
        assertNotNull(updatedTodo);
        assertEquals("Updated Todo", updatedTodo.getTitle());
    }

    @Test
    public void testTodoDeletion() {
        Todo todo = todoRepository.findById("1").orElse(null);
        assertNotNull(todo);
        todoRepository.delete(todo);

        Todo deletedTodo = todoRepository.findById("1").orElse(null);
        assertNull(deletedTodo);
    }
}
```