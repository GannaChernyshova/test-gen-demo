```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TodoControllerTest {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer()
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository repository;

    @Test
    public void testGetAll() {
        Iterable<Todo> todos = restTemplate.getForObject("/todos", Iterable.class);
        assertNotNull(todos);
    }

    @Test
    public void testGetById() {
        Todo todo = new Todo();
        todo = repository.save(todo);
        ResponseEntity<Todo> response = restTemplate.getForEntity("/todos/" + todo.getId(), Todo.class);
        assertEquals(todo.getId(), response.getBody().getId());
    }

    @Test
    public void testSave() {
        Todo todo = new Todo();
        ResponseEntity<Todo> response = restTemplate.postForEntity("/todos", todo, Todo.class);
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void testUpdate() {
        Todo todo = new Todo();
        todo = repository.save(todo);
        todo.setTitle("Updated");
        restTemplate.put("/todos/" + todo.getId(), todo);
        Todo updatedTodo = repository.findById(todo.getId()).get();
        assertEquals("Updated", updatedTodo.getTitle());
    }

    @Test
    public void testDeleteById() {
        Todo todo = new Todo();
        todo = repository.save(todo);
        restTemplate.delete("/todos/" + todo.getId());
        assertFalse(repository.findById(todo.getId()).isPresent());
    }

    @Test
    public void testDeleteAll() {
        restTemplate.delete("/todos");
        assertFalse(repository.findAll().iterator().hasNext());
    }
}
```