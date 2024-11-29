package com.atomicjar.todos.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoNotFoundExceptionIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa")
            .withReuse(true);

    @BeforeEach
    public void setUp() {
        // Initialize your database here if needed
    }

    @Test
    public void whenTodoNotFound_thenReturns404() {
        String id = "non-existing-id";
        ResponseEntity<String> response = restTemplate.getForEntity("/todos/" + id, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Todo not found with id: " + id, response.getBody());
    }
}

This is a basic example of an integration test using TestContainers and Spring Boot Test. The test class is testing the `TodoNotFoundException` class. The `setUp` method can be used to initialize the database before each test if needed. The `whenTodoNotFound_thenReturns404` test method sends a GET request to a non-existing todo and asserts that the response status is 404 NOT FOUND and the response body contains the expected error message.