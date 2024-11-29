```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
public class TodoNotFoundExceptionTest {

    private TodoNotFoundException todoNotFoundException;

    @BeforeEach
    public void setUp() {
        todoNotFoundException = new TodoNotFoundException("TestID");
    }

    @Test
    public void testTodoNotFoundException() {
        assertEquals("TestID", todoNotFoundException.getMessage());
    }

    @AfterEach
    public void tearDown() {
        todoNotFoundException = null;
    }
}
```