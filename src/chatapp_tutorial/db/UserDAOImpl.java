package chatapp_tutorial.db;

import chatapp_tutorial.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════
 * DB TUTORIAL – JDBC implementation of UserDAO
 * ═══════════════════════════════════════════════════════════════════
 *
 * CONCEPTS: Same as MessageDAOImpl – PreparedStatement, ResultSet,
 *           try-with-resources.
 *
 * NEW CONCEPT: INSERT IGNORE
 *   The users table has a UNIQUE constraint on the username column.
 *   Normally, inserting a duplicate username would throw a SQLException.
 *   "INSERT IGNORE" tells MySQL to silently skip the row instead.
 *   This lets us safely call save() every time a user connects, even
 *   if they have connected before.
 *
 *   PostgreSQL equivalent:  INSERT INTO users ... ON CONFLICT DO NOTHING
 *   H2 equivalent:          MERGE INTO users KEY(username) VALUES (?)
 * ═══════════════════════════════════════════════════════════════════
 */
public class UserDAOImpl implements UserDAO {

    private final Connection connection;

    /** TODO 1: Store connection in the field. */
    public UserDAOImpl(Connection connection) {
        // TODO 1
    }

    // ─────────────────────────────────────────────────────────────────────────
    // save
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 2: INSERT the user's username into the users table.
     *
     * SQL:
     *   INSERT IGNORE INTO users (username) VALUES (?)
     *
     * Steps:
     *   a) Prepare the SQL.
     *   b) Set parameter 1 to user.getUsername().
     *   c) Call executeUpdate().
     */
    @Override
    public void save(User user) throws SQLException {
        // TODO 2
    }

    // ─────────────────────────────────────────────────────────────────────────
    // findAll
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 3: SELECT all users ordered by connection time.
     *
     * SQL:
     *   SELECT id, username, connected_at FROM users ORDER BY connected_at
     *
     * For each row, create:
     *   new User(rs.getInt("id"),
     *            rs.getString("username"),
     *            rs.getTimestamp("connected_at").toLocalDateTime())
     *
     * Return the list.
     */
    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        // TODO 3
        return users;
    }
}
