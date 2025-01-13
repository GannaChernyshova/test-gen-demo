package com.atomicjar.todos.repository;

import com.atomicjar.todos.entity.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class TodoRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"))
            .withReuse(true);

    @Autowired
    TodoRepository todoRepository;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        Todo todo1 = new Todo();
        todo1.setTitle("First Todo");
        todo1.setCompleted(false);

        Todo todo2 = new Todo();
        todo2.setTitle("Second Todo");
        todo2.setCompleted(true);

        todoRepository.saveAll(List.of(todo1, todo2));
    }

    @AfterEach
    void tearDown() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldGetPendingTodos() {
        List<Todo> pendingTodos = todoRepository.getPendingTodos();
        
        assertThat(pendingTodos).hasSize(1);
        assertThat(pendingTodos.get(0).getTitle()).isEqualTo("First Todo");
        assertThat(pendingTodos.get(0).isCompleted()).isFalse();
    }

    @Test
    void shouldGetCompletedTodos() {
        List<Todo> completedTodos = todoRepository.getCompletedTodos();
        
        assertThat(completedTodos).hasSize(1);
        assertThat(completedTodos.get(0).getTitle()).isEqualTo("Second Todo");
        assertThat(completedTodos.get(0).isCompleted()).isTrue();
    }

    @Test
    void shouldSaveAndRetrieveTodo() {
        Todo todo = new Todo();
        todo.setTitle("Test Todo");
        todo.setCompleted(false);

        Todo saved = todoRepository.save(todo);
        Todo retrieved = todoRepository.findById(saved.getId()).orElseThrow();

        assertThat(retrieved.getTitle()).isEqualTo("Test Todo");
        assertThat(retrieved.isCompleted()).isFalse();
    }
}