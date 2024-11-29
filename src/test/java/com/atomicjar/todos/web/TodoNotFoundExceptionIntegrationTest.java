```java
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
public class TodoNotFoundExceptionTest {

    @Test
    public void testTodoNotFoundException() {
        String id = "testId";
        Exception exception = assertThrows(TodoNotFoundException.class, () -> {
            throw new TodoNotFoundException(id);
        });

        String expectedMessage = "Todo not found with id: " + id;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);

        if (exception instanceof ResponseStatusException) {
            assertEquals(HttpStatus.NOT_FOUND, ((ResponseStatusException) exception).getStatus());
        }
    }
}
```