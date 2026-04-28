package chatapp_tutorial.db;

import chatapp_tutorial.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════
 * DB TUTORIAL – JDBC implementation of MessageDAO
 * ═══════════════════════════════════════════════════════════════════
 *
 * CONCEPTS:
 *   - PreparedStatement  (safer and faster than plain Statement)
 *   - ResultSet iteration
 *   - try-with-resources (auto-close DB resources)
 *
 * WHY PreparedStatement instead of plain Statement?
 *   With a plain Statement you would write:
 *     "INSERT INTO messages VALUES ('" + content + "')"
 *   If content contains  '; DROP TABLE messages; --
 *   the database will execute that as real SQL → SQL INJECTION attack!
 *
 *   PreparedStatement treats the ? placeholders as DATA, never as SQL,
 *   so the injection string is stored as a harmless literal string.
 * ═══════════════════════════════════════════════════════════════════
 */
public class MessageDAOImpl implements MessageDAO {

    private final Connection connection;

    /** TODO 1: Store the connection in the field (this.connection = connection). */
    public MessageDAOImpl(Connection connection) {
        // TODO 1
    }

    // ─────────────────────────────────────────────────────────────────────────
    // save
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 2: INSERT a new message row.
     *
     * SQL to use:
     *   INSERT INTO messages (username, content) VALUES (?, ?)
     *
     * Steps:
     *   a) Create a PreparedStatement from the SQL string above.
     *      Use try-with-resources so it closes automatically.
     *   b) Set parameter 1 (username):  ps.setString(1, message.getUsername())
     *   c) Set parameter 2 (content):   ps.setString(2, message.getContent())
     *   d) Execute:                      ps.executeUpdate()
     *
     * HINT:
     *   String sql = "INSERT INTO messages (username, content) VALUES (?, ?)";
     *   try (PreparedStatement ps = connection.prepareStatement(sql)) {
     *       ps.setString(1, ...);
     *       ps.setString(2, ...);
     *       ps.executeUpdate();
     *   }
     */
    @Override
    public void save(Message message) throws SQLException {
        // TODO 2
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getRecent
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 3: SELECT the last <limit> messages, returned oldest-first.
     *
     * The SQL uses a subquery trick:
     *   Inner query:  ORDER BY sent_at DESC  LIMIT ?   → gets the NEWEST rows
     *   Outer query:  ORDER BY sent_at ASC             → flips to oldest-first
     *
     * Full SQL:
     *   SELECT id, username, content, sent_at
     *   FROM (
     *     SELECT id, username, content, sent_at
     *     FROM messages
     *     ORDER BY sent_at DESC
     *     LIMIT ?
     *   ) sub
     *   ORDER BY sent_at ASC
     *
     * Steps:
     *   a) Prepare the SQL.
     *   b) Set the LIMIT parameter:  ps.setInt(1, limit)
     *   c) Execute query and get ResultSet.
     *   d) For each row in the ResultSet, create a Message object:
     *        new Message(rs.getInt("id"),
     *                    rs.getString("username"),
     *                    rs.getString("content"),
     *                    rs.getTimestamp("sent_at").toLocalDateTime())
     *   e) Add each Message to a List and return the list.
     *
     * HINT: Use two nested try-with-resources:
     *   try (PreparedStatement ps = ...) {
     *       ps.setInt(1, limit);
     *       try (ResultSet rs = ps.executeQuery()) {
     *           while (rs.next()) { ... }
     *       }
     *   }
     */
    @Override
    public List<Message> getRecent(int limit) throws SQLException {
        List<Message> result = new ArrayList<>();
        // TODO 3
        return result;
    }
}
