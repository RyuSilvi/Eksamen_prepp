package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Example 3 – Data Access Object (DAO) Pattern
 *
 * The DAO pattern separates database logic from business logic.
 * Each entity (e.g. User) has its own DAO class that handles all
 * CRUD operations for that entity.
 *
 * Structure:
 *   User            – plain model / POJO
 *   UserDAO         – interface that defines operations
 *   UserDAOImpl     – JDBC implementation of UserDAO
 *   DAOExample      – main class that wires everything together
 *
 * This pattern makes it easy to:
 *   - Swap the database (only change the Impl class)
 *   - Unit-test business logic by mocking the DAO interface
 */
public class DAOExample {

    // =========================================================================
    // Model (POJO)
    // =========================================================================
    public static class User {
        private int    id;
        private String name;
        private String email;

        public User(int id, String name, String email) {
            this.id    = id;
            this.name  = name;
            this.email = email;
        }

        // Convenience constructor for INSERT (id not yet known)
        public User(String name, String email) {
            this(0, name, email);
        }

        public int    getId()    { return id; }
        public String getName()  { return name; }
        public String getEmail() { return email; }

        @Override
        public String toString() {
            return String.format("User{id=%d, name='%s', email='%s'}", id, name, email);
        }
    }

    // =========================================================================
    // DAO Interface – defines what operations are possible
    // =========================================================================
    public interface UserDAO {
        void           save(User user)         throws SQLException;
        Optional<User> findById(int id)        throws SQLException;
        List<User>     findAll()               throws SQLException;
        void           update(User user)       throws SQLException;
        void           delete(int id)          throws SQLException;
    }

    // =========================================================================
    // JDBC Implementation of UserDAO
    // =========================================================================
    public static class UserDAOImpl implements UserDAO {

        private final Connection connection;

        public UserDAOImpl(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void save(User user) throws SQLException {
            String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.executeUpdate();
            }
        }

        @Override
        public Optional<User> findById(int id) throws SQLException {
            String sql = "SELECT id, name, email FROM users WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(new User(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("email")));
                    }
                }
            }
            return Optional.empty();
        }

        @Override
        public List<User> findAll() throws SQLException {
            List<User> users = new ArrayList<>();
            String sql = "SELECT id, name, email FROM users";
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email")));
                }
            }
            return users;
        }

        @Override
        public void update(User user) throws SQLException {
            String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setInt(3, user.getId());
                ps.executeUpdate();
            }
        }

        @Override
        public void delete(int id) throws SQLException {
            String sql = "DELETE FROM users WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        }
    }

    // =========================================================================
    // Main – demonstrate the DAO pattern
    // =========================================================================
    private static final String DB_URL   = "jdbc:mysql://localhost:3306/my_database";
    private static final String USER_DB  = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER_DB, PASSWORD)) {

            UserDAO dao = new UserDAOImpl(conn);

            // Create
            dao.save(new User("Charlie", "charlie@example.com"));
            dao.save(new User("Diana",   "diana@example.com"));

            // Read all
            System.out.println("All users:");
            dao.findAll().forEach(System.out::println);

            // Read one
            dao.findById(1).ifPresent(u -> System.out.println("Found: " + u));

            // Update
            dao.update(new User(1, "Charlie Brown", "charlie.brown@example.com"));

            // Delete
            dao.delete(2);

            System.out.println("After update and delete:");
            dao.findAll().forEach(System.out::println);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
