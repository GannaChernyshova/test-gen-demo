package com.atomicjar.todos.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class TodoRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa")
            .withReuse(true);

    @BeforeEach
    public void init() {
        Todo todo1 = new Todo();
        todo1.setCompleted(false);
        entityManager.persist(todo1);

        Todo todo2 = new Todo();
        todo2.setCompleted(true);
        entityManager.persist(todo2);

        entityManager.flush();
    }

    @Test
    public void testGetPendingTodos() {
        List<Todo> todos = todoRepository.getPendingTodos();
        assertThat(todos).hasSize(1);
        assertThat(todos.get(0).isCompleted()).isFalse();
    }

    @Test
    public void testGetCompletedTodos() {
        List<Todo> todos = todoRepository.getCompletedTodos();
        assertThat(todos).hasSize(1);
        assertThat(todos.get(0).isCompleted()).isTrue();
    }
}


Please note that this is a basic example and might need adjustments based on your actual `Todo` entity and `TodoRepository` interface. Also, the `Todo` entity is assumed to have a `boolean` field named `completed` and corresponding getter and setter methods.