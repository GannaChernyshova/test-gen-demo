package com.atomicjar.todos.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TodoControllerIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository repository;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();
        repository.save(new Todo("Test Todo"));
    }

    @Test
    public void getAll() {
        ResponseEntity<Todo[]> response = restTemplate.getForEntity("/todos", Todo[].class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    public void getById() {
        Todo todo = repository.findAll().iterator().next();
        ResponseEntity<Todo> response = restTemplate.getForEntity("/todos/" + todo.getId(), Todo.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getId()).isEqualTo(todo.getId());
    }

    @Test
    public void save() {
        Todo todo = new Todo("New Todo");
        ResponseEntity<Todo> response = restTemplate.postForEntity("/todos", todo, Todo.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getTitle()).isEqualTo(todo.getTitle());
    }

    @Test
    public void update() {
        Todo todo = repository.findAll().iterator().next();
        todo.setTitle("Updated Todo");
        restTemplate.put("/todos/" + todo.getId(), todo);
        Todo updatedTodo = repository.findById(todo.getId()).orElse(null);
        assertThat(updatedTodo.getTitle()).isEqualTo("Updated Todo");
    }

    @Test
    public void deleteById() {
        Todo todo = repository.findAll().iterator().next();
        restTemplate.delete("/todos/" + todo.getId());
        assertThat(repository.existsById(todo.getId())).isFalse();
    }

    @Test
    public void deleteAll() {
        restTemplate.delete("/todos");
        assertThat(repository.count()).isEqualTo(0);
    }
}