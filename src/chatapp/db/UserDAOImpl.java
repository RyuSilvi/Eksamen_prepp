package chatapp.db;

import chatapp.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DB – JDBC implementation of UserDAO.
 *
 * "INSERT IGNORE" (MySQL syntax) silently skips the INSERT if the username
 * already exists (the username column has a UNIQUE constraint).
 * For PostgreSQL use: INSERT INTO users ... ON CONFLICT DO NOTHING
 * For H2 use:         MERGE INTO users ...
 */
public class UserDAOImpl implements UserDAO {

    private final Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(User user) throws SQLException {
        // INSERT IGNORE skips duplicate usernames without throwing an exception
        String sql = "INSERT IGNORE INTO users (username) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.executeUpdate();
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, connected_at FROM users ORDER BY connected_at";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getTimestamp("connected_at").toLocalDateTime()
                ));
            }
        }
        return users;
    }
}
