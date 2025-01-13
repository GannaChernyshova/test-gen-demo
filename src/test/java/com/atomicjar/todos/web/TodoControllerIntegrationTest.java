package com.atomicjar.todos.web;

import com.atomicjar.todos.entity.Todo;
import com.atomicjar.todos.repository.TodoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class TodoControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withReuse(true);

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate rest;

    @Autowired
    private TodoRepository repository;

    private String baseUrl;
    private Todo testTodo;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/todos";
        testTodo = repository.save(new Todo("Test todo", 1L, false));
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void shouldGetAllTodos() {
        ResponseEntity<Todo[]> response = rest.getForEntity(baseUrl, Todo[].class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getTitle()).isEqualTo("Test todo");
    }

    @Test
    void shouldGetTodoById() {
        ResponseEntity<Todo> response = rest.getForEntity(baseUrl + "/" + testTodo.getId(), Todo.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Test todo");
    }

    @Test
    void shouldCreateNewTodo() {
        Todo newTodo = new Todo("New todo", 2L, false);
        
        ResponseEntity<Todo> response = rest.postForEntity(baseUrl, newTodo, Todo.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getTitle()).isEqualTo("New todo");
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    @Test
    void shouldUpdateExistingTodo() {
        Todo updateTodo = new Todo("Updated todo", null, true);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Todo> request = new HttpEntity<>(updateTodo, headers);
        
        ResponseEntity<Todo> response = rest.exchange(
                baseUrl + "/" + testTodo.getId(),
                HttpMethod.PATCH,
                request,
                Todo.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Updated todo");
        assertThat(response.getBody().getCompleted()).isTrue();
        assertThat(response.getBody().getOrder()).isEqualTo(1L);
    }

    @Test
    void shouldDeleteTodoById() {
        ResponseEntity<Void> response = rest.exchange(
                baseUrl + "/" + testTodo.getId(),
                HttpMethod.DELETE,
                null,
                Void.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.findById(testTodo.getId())).isEmpty();
    }

    @Test
    void shouldDeleteAllTodos() {
        ResponseEntity<Void> response = rest.exchange(
                baseUrl,
                HttpMethod.DELETE,
                null,
                Void.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(repository.count()).isZero();
    }
}