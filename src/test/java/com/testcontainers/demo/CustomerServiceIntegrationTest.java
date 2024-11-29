```java
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class CustomerServiceTest {

    @Container
    public PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    private DBConnectionProvider connectionProvider;
    private CustomerService customerService;

    @BeforeEach
    public void setUp() {
        connectionProvider = new DBConnectionProvider(postgreSQLContainer.getJdbcUrl(), postgreSQLContainer.getUsername(), postgreSQLContainer.getPassword());
        customerService = new CustomerService(connectionProvider);
        customerService.createCustomersTableIfNotExists();
    }

    @AfterEach
    public void tearDown() {
        customerService.deleteAllCustomers();
    }

    @Test
    public void testCreateCustomer() {
        Customer customer = new Customer(1L, "John Doe", "john.doe@example.com");
        customerService.createCustomer(customer);

        Optional<Customer> retrievedCustomer = customerService.getCustomer(1L);
        assertTrue(retrievedCustomer.isPresent());
        assertEquals(customer, retrievedCustomer.get());
    }

    @Test
    public void testGetCustomer() {
        Customer customer = new Customer(2L, "Jane Doe", "jane.doe@example.com");
        customerService.createCustomer(customer);

        Optional<Customer> retrievedCustomer = customerService.getCustomer(2L);
        assertTrue(retrievedCustomer.isPresent());
        assertEquals(customer, retrievedCustomer.get());
    }

    @Test
    public void testGetAllCustomers() {
        Customer customer1 = new Customer(3L, "Alice", "alice@example.com");
        Customer customer2 = new Customer(4L, "Bob", "bob@example.com");
        customerService.createCustomer(customer1);
        customerService.createCustomer(customer2);

        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2, customers.size());
        assertTrue(customers.contains(customer1));
        assertTrue(customers.contains(customer2));
    }

    @Test
    public void testDeleteAllCustomers() {
        Customer customer1 = new Customer(5L, "Charlie", "charlie@example.com");
        Customer customer2 = new Customer(6L, "Dave", "dave@example.com");
        customerService.createCustomer(customer1);
        customerService.createCustomer(customer2);

        customerService.deleteAllCustomers();

        List<Customer> customers = customerService.getAllCustomers();
        assertTrue(customers.isEmpty());
    }
}
```