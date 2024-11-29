```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TodoControllerTest {

    @Container
    private static final TodoRepository todoRepository = new TodoRepository();

    @Autowired
    private TestRestTemplate restTemplate;

    private Todo todo;

    @BeforeEach
    public void setUp() {
        todo = new Todo();
        todo.setId("1");
        todo.setTitle("Test Todo");
        todo.setDescription("Test Description");
        todoRepository.save(todo);
    }

    @AfterEach
    public void tearDown() {
        todoRepository.deleteAll();
    }

    @Test
    public void testGetAll() {
        Iterable<Todo> todos = restTemplate.getForObject("/todos", Iterable.class);
        assertNotNull(todos);
    }

    @Test
    public void testGetById() {
        ResponseEntity<Todo> response = restTemplate.getForEntity("/todos/1", Todo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(todo, response.getBody());
    }

    @Test
    public void testSave() {
        Todo newTodo = new Todo();
        newTodo.setTitle("New Todo");
        newTodo.setDescription("New Description");
        ResponseEntity<Todo> response = restTemplate.postForEntity("/todos", newTodo, Todo.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void testUpdate() {
        Todo updatedTodo = new Todo();
        updatedTodo.setTitle("Updated Todo");
        updatedTodo.setDescription("Updated Description");
        restTemplate.patchForObject("/todos/1", updatedTodo, Todo.class);
        Todo retrievedTodo = todoRepository.findById("1").orElse(null);
        assertEquals(updatedTodo, retrievedTodo);
    }

    @Test
    public void testDeleteById() {
        restTemplate.delete("/todos/1");
        Todo retrievedTodo = todoRepository.findById("1").orElse(null);
        assertEquals(null, retrievedTodo);
    }

    @Test
    public void testDeleteAll() {
        restTemplate.delete("/todos");
        Iterable<Todo> todos = restTemplate.getForObject("/todos", Iterable.class);
        assertEquals(null, todos);
    }
}
```