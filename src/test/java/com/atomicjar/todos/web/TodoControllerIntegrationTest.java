package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {TodoControllerIntegrationTest.Initializer.class})
@Testcontainers
public class TodoControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("integration-tests-db")
            .withUsername("username")
            .withPassword("password")
            .withReuse(true);

    @Autowired
    private TodoController todoController;

    @TestConfiguration
    static class Config {
        // application.properties setup for test
    }

    @BeforeEach
    public void setUp() {
        Todo todo = new Todo();
        todoController.save(todo);
    }

    @Test
    public void testGetAll() {
        Iterable<Todo> todos = todoController.getAll();
        assertNotNull(todos);
    }

    @Test
    public void testGetById() {
        ResponseEntity<Todo> todo = todoController.getById("1");
        assertEquals(200, todo.getStatusCodeValue());
    }

    @Test
    public void testSave() {
        Todo todo = new Todo();
        ResponseEntity<Todo> savedTodo = todoController.save(todo);
        assertEquals(200, savedTodo.getStatusCodeValue());
    }

    @Test
    public void testUpdate() {
        Todo todo = new Todo();
        ResponseEntity<Todo> updatedTodo = todoController.update("1", todo);
        assertEquals(200, updatedTodo.getStatusCodeValue());
    }

    @Test
    public void testDeleteById() {
        ResponseEntity<Void> responseEntity = todoController.deleteById("1");
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testDeleteAll() {
        ResponseEntity<Void> responseEntity = todoController.deleteAll();
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.jpa.hibernate.ddl-auto=create-drop",
                    "spring.jpa.show-sql=true"
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}