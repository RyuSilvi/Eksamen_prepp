package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Example 1 – Basic JDBC Connection
 *
 * Shows how to:
 *  - Open a connection to a database (MySQL shown; swap URL/driver for other DBs)
 *  - Execute a simple SELECT query
 *  - Iterate over the ResultSet
 *  - Always close resources in a finally-block (or use try-with-resources)
 *
 * To run:
 *  1. Add your JDBC driver (e.g. mysql-connector-java) to the classpath.
 *  2. Replace DB_URL, USER, and PASSWORD with your credentials.
 *  3. Replace the SQL with a real table name from your schema.
 */
public class BasicJDBCConnection {

    // --- Configuration – change these to match your database ---
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/my_database";
    private static final String USER    = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {

        // try-with-resources ensures Connection, Statement, and ResultSet are
        // closed automatically even when an exception is thrown.
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement  statement  = connection.createStatement();
             ResultSet  resultSet  = statement.executeQuery("SELECT id, name, email FROM users")) {

            System.out.println("Connected to the database!");

            // Iterate through the rows returned by the query
            while (resultSet.next()) {
                int    id    = resultSet.getInt("id");
                String name  = resultSet.getString("name");
                String email = resultSet.getString("email");

                System.out.printf("ID: %d | Name: %s | Email: %s%n", id, name, email);
            }

        } catch (SQLException e) {
            // Always print enough information to diagnose the problem
            System.err.println("Database error: " + e.getMessage());
            System.err.println("SQLState: "        + e.getSQLState());
            System.err.println("Error code: "      + e.getErrorCode());
            e.printStackTrace();
        }
    }
}
