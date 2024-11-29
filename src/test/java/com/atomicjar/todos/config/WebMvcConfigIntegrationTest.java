```java
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Testcontainers
public class WebMvcConfigTest {

    @Autowired
    private WebMvcConfig webMvcConfig;

    @Test
    public void testAddCorsMappings() {
        CorsRegistry corsRegistry = new CorsRegistry();
        webMvcConfig.addCorsMappings(corsRegistry);
        CorsConfiguration corsConfiguration = corsRegistry.getCorsConfigurations().get("*");
        assertNotNull(corsConfiguration);
    }
}
```