```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
public class WebMvcConfigTest {

    @Autowired
    private WebMvcConfig webMvcConfig;

    private CorsRegistry corsRegistry;

    @BeforeEach
    public void setUp() {
        corsRegistry = new CorsRegistry();
    }

    @AfterEach
    public void tearDown() {
        corsRegistry = null;
    }

    @Test
    public void testAddCorsMappings() {
        webMvcConfig.addCorsMappings(corsRegistry);
        CorsConfiguration corsConfiguration = corsRegistry.getCorsConfigurations().get("/**");

        assertEquals("*", corsConfiguration.getAllowedOrigins().get(0));
        assertEquals("*", corsConfiguration.getAllowedMethods().get(0));
        assertEquals("*", corsConfiguration.getAllowedHeaders().get(0));
        assertEquals(1800, corsConfiguration.getMaxAge());
    }
}
```