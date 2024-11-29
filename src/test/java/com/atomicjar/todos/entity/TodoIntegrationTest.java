package com.atomicjar.todos.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TodoIntegrationTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("integration-tests-db")
            .withUsername("username")
            .withPassword("password")
            .withReuse(true);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository todoRepository;

    @BeforeEach
    public void setUp() {
        todoRepository.deleteAll();

        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setCompleted(false);
        todo.setOrder(1);

        todoRepository.save(todo);
    }

    @Test
    public void testGetTodo() {
        Todo todo = restTemplate.getForObject("/todos/1", Todo.class);
        assertThat(todo.getTitle()).isEqualTo("Test Todo");
        assertThat(todo.getCompleted()).isFalse();
        assertThat(todo.getOrder()).isEqualTo(1);
    }

    // Add more tests as needed
}

Please note that you need to replace `"username"` and `"password"` with your actual PostgreSQL username and password. Also, replace `"/todos/1"` with your actual API endpoint. You may also need to add more tests as needed.