package com.atomicjar.todos.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebMvcConfigIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Test
    public void testAddCorsMappings() {
        CorsConfiguration corsConfiguration = corsConfigurationSource.getCorsConfiguration(restTemplate.getRootUri());
        assertNotNull(corsConfiguration, "Cors configuration should not be null");
    }
}

Please note that this is a basic test case and might need to be adjusted based on the actual implementation of the `addCorsMappings` method in the `WebMvcConfig` class. The test case assumes that the `addCorsMappings` method adds a `CorsConfiguration` to a `CorsConfigurationSource` bean. The test case then retrieves the `CorsConfiguration` from the `CorsConfigurationSource` and checks if it is not null.