```java
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
public class ApplicationTest {

    @Container
    public static GenericContainer<?> app = new GenericContainer<>(DockerImageName.parse("application:latest"))
            .withExposedPorts(8080);

    @BeforeAll
    public static void setUp() {
        app.start();
    }

    @DynamicPropertySource
    public static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("app.url", () -> "http://localhost:" + app.getMappedPort(8080));
    }

    @Test
    public void testMain() {
        // TODO: Add realistic test data and assertions
    }

    @AfterAll
    public static void tearDown() {
        app.stop();
    }
}
```