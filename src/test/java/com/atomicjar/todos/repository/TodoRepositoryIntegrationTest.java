package com.atomicjar.todos.repository;

import com.atomicjar.todos.entity.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TodoRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
            .withReuse(true);

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private Todo todo;

    @BeforeEach
    public void setUp() {
        todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setCompleted(false);
        todoRepository.save(todo);
    }

    @AfterEach
    public void tearDown() {
        todoRepository.deleteAll();
    }

    @Test
    public void testGetPendingTodos() {
        List<Todo> todos = todoRepository.getPendingTodos();
        assertThat(todos).isNotEmpty();
        assertThat(todos.get(0).getTitle()).isEqualTo("Test Todo");
        assertThat(todos.get(0).isCompleted()).isFalse();
    }

    @Test
    public void testGetCompletedTodos() {
        todo.setCompleted(true);
        todoRepository.save(todo);

        List<Todo> todos = todoRepository.getCompletedTodos();
        assertThat(todos).isNotEmpty();
        assertThat(todos.get(0).getTitle()).isEqualTo("Test Todo");
        assertThat(todos.get(0).isCompleted()).isTrue();
    }
}