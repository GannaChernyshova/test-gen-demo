```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest
public class TodoTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
            .withUsername("test")
            .withPassword("test")
            .withDatabaseName("test");

    @Autowired
    private TodoRepository todoRepository;

    private Todo todo;

    @BeforeEach
    public void setUp() {
        todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setCompleted(false);
        todo.setOrder(1);
    }

    @AfterEach
    public void tearDown() {
        todoRepository.deleteAll();
        todo = null;
    }

    @Test
    public void testGetId() {
        Todo savedTodo = todoRepository.save(todo);
        assertNotNull(savedTodo.getId());
    }

    @Test
    public void testSetId() {
        todo.setId("123");
        Todo savedTodo = todoRepository.save(todo);
        assertEquals("123", savedTodo.getId());
    }

    @Test
    public void testGetTitle() {
        Todo savedTodo = todoRepository.save(todo);
        assertEquals("Test Todo", savedTodo.getTitle());
    }

    @Test
    public void testSetTitle() {
        todo.setTitle("New Title");
        Todo savedTodo = todoRepository.save(todo);
        assertEquals("New Title", savedTodo.getTitle());
    }

    @Test
    public void testGetCompleted() {
        Todo savedTodo = todoRepository.save(todo);
        assertEquals(false, savedTodo.getCompleted());
    }

    @Test
    public void testSetCompleted() {
        todo.setCompleted(true);
        Todo savedTodo = todoRepository.save(todo);
        assertEquals(true, savedTodo.getCompleted());
    }

    @Test
    public void testGetOrder() {
        Todo savedTodo = todoRepository.save(todo);
        assertEquals(1, savedTodo.getOrder());
    }

    @Test
    public void testSetOrder() {
        todo.setOrder(2);
        Todo savedTodo = todoRepository.save(todo);
        assertEquals(2, savedTodo.getOrder());
    }

    @Test
    public void testGetUrl() {
        Todo savedTodo = todoRepository.save(todo);
        assertNotNull(savedTodo.getUrl());
    }
}
```