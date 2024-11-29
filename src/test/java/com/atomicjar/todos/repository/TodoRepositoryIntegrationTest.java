package com.example.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class TodoRepositoryIntegrationTest {

    @Container
    public static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:latest")
            .withReuse(true);

    @Autowired
    private TodoRepository todoRepository;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
    }

    @BeforeEach
    void setUp() {
        Todo todo1 = new Todo();
        todo1.setStatus("Pending");
        todoRepository.save(todo1);

        Todo todo2 = new Todo();
        todo2.setStatus("Completed");
        todoRepository.save(todo2);
    }

    @Test
    void getPendingTodos() {
        List<Todo> pendingTodos = todoRepository.getPendingTodos();
        assertEquals(1, pendingTodos.size());
    }

    @Test
    void getCompletedTodos() {
        List<Todo> completedTodos = todoRepository.getCompletedTodos();
        assertEquals(1, completedTodos.size());
    }
}