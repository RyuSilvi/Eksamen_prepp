package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Example 2 – PreparedStatement (INSERT, UPDATE, DELETE, SELECT with parameters)
 *
 * Why use PreparedStatement instead of Statement?
 *  - Prevents SQL injection attacks (values are never interpreted as SQL)
 *  - Better performance for repeated queries (query is compiled once)
 *  - Cleaner code when mixing Java values into SQL
 *
 * Table assumed:
 *   CREATE TABLE users (
 *       id    INT PRIMARY KEY AUTO_INCREMENT,
 *       name  VARCHAR(100),
 *       email VARCHAR(100)
 *   );
 */
public class PreparedStatementExample {

    private static final String DB_URL   = "jdbc:mysql://localhost:3306/my_database";
    private static final String USER     = "root";
    private static final String PASSWORD = "password";

    // -------------------------------------------------------------------------
    // INSERT – add a new user
    // -------------------------------------------------------------------------
    public static void insertUser(Connection conn, String name, String email)
            throws SQLException {

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);   // first  ?
            ps.setString(2, email);  // second ?
            int rowsAffected = ps.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " row(s). Name=" + name);
        }
    }

    // -------------------------------------------------------------------------
    // SELECT – find users by name
    // -------------------------------------------------------------------------
    public static void findByName(Connection conn, String name) throws SQLException {

        String sql = "SELECT id, name, email FROM users WHERE name = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Users named '" + name + "':");
                while (rs.next()) {
                    System.out.printf("  id=%d  email=%s%n",
                            rs.getInt("id"), rs.getString("email"));
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // UPDATE – change e-mail address by user id
    // -------------------------------------------------------------------------
    public static void updateEmail(Connection conn, int userId, String newEmail)
            throws SQLException {

        String sql = "UPDATE users SET email = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newEmail);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Updated " + rowsAffected + " row(s) for id=" + userId);
        }
    }

    // -------------------------------------------------------------------------
    // DELETE – remove a user by id
    // -------------------------------------------------------------------------
    public static void deleteUser(Connection conn, int userId) throws SQLException {

        String sql = "DELETE FROM users WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            System.out.println("Deleted " + rowsAffected + " row(s) for id=" + userId);
        }
    }

    // -------------------------------------------------------------------------
    // main – demo all operations inside a transaction
    // -------------------------------------------------------------------------
    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {

            // Disable auto-commit so we can roll back on error
            conn.setAutoCommit(false);

            try {
                insertUser(conn, "Alice", "alice@example.com");
                insertUser(conn, "Bob",   "bob@example.com");

                findByName(conn, "Alice");

                updateEmail(conn, 1, "alice.updated@example.com");

                deleteUser(conn, 2);

                // Commit all changes at once
                conn.commit();
                System.out.println("Transaction committed.");

            } catch (SQLException e) {
                // Something went wrong – undo every change in this transaction
                conn.rollback();
                System.err.println("Transaction rolled back: " + e.getMessage());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
