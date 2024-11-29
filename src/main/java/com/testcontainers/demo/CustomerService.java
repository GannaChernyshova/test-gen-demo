package com.testcontainers.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerService {

    private final DBConnectionProvider connectionProvider;

    public CustomerService(DBConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        createCustomersTableIfNotExists();
    }

    public void createCustomer(Customer customer) {
        try (Connection conn = this.connectionProvider.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "insert into customers(id,name) values(?,?)"
            );
            pstmt.setLong(1, customer.id());
            pstmt.setString(2, customer.name());
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Customer> getCustomer(Long id) {
        try (Connection conn = this.connectionProvider.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT id, name FROM customers WHERE id = ?"
            );
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                return Optional.of(new Customer(id, name));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving customer with id " + id, e);
        }


        return Optional.empty();
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = this.connectionProvider.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    "select id,name from customers"
            );
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                customers.add(new Customer(id, name));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return customers;
    }

    public void deleteAllCustomers() {
        String sql = "DELETE FROM customers";
        try (Connection conn = this.connectionProvider.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customers", e);
        }
    }


    private void createCustomersTableIfNotExists() {
        try (Connection conn = this.connectionProvider.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(
                    """
                            create table if not exists customers (
                                id bigint not null,
                                name varchar not null,
                                primary key (id)
                            )
                            """
            );
            pstmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}