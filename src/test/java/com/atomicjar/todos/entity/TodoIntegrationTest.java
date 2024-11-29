package com.atomicjar.todos.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class TodoTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true);

    @DynamicPropertySource
    public static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
    }

    private Todo todo;

    @BeforeEach
    public void setUp() {
        todo = new Todo();
        todo.setId("1");
        todo.setTitle("Test Todo");
        todo.setCompleted(false);
        todo.setOrder(1);
    }

    @Test
    public void testGetId() {
        assertEquals("1", todo.getId());
    }

    @Test
    public void testSetId() {
        todo.setId("2");
        assertEquals("2", todo.getId());
    }

    @Test
    public void testGetTitle() {
        assertEquals("Test Todo", todo.getTitle());
    }

    @Test
    public void testSetTitle() {
        todo.setTitle("Updated Todo");
        assertEquals("Updated Todo", todo.getTitle());
    }

    @Test
    public void testGetCompleted() {
        assertFalse(todo.getCompleted());
    }

    @Test
    public void testSetCompleted() {
        todo.setCompleted(true);
        assertTrue(todo.getCompleted());
    }

    @Test
    public void testGetOrder() {
        assertEquals(1, todo.getOrder());
    }

    @Test
    public void testSetOrder() {
        todo.setOrder(2);
        assertEquals(2, todo.getOrder());
    }
}