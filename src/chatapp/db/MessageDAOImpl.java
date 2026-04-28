package chatapp.db;

import chatapp.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DB – JDBC implementation of MessageDAO.
 *
 * Uses PreparedStatement for every query to prevent SQL injection.
 * A PreparedStatement pre-compiles the SQL; user input is passed as
 * parameters (?) and is never concatenated into the SQL string.
 */
public class MessageDAOImpl implements MessageDAO {

    private final Connection connection;

    public MessageDAOImpl(Connection connection) {
        this.connection = connection;
    }

    // =========================================================================
    // save – INSERT a new message
    // =========================================================================
    @Override
    public void save(Message message) throws SQLException {
        String sql = "INSERT INTO messages (username, content) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, message.getUsername());
            ps.setString(2, message.getContent());
            ps.executeUpdate();
        }
    }

    // =========================================================================
    // getRecent – SELECT the last <limit> messages, oldest-first
    //
    // Why the subquery?
    //   We need the NEWEST rows (ORDER BY sent_at DESC LIMIT n),
    //   but we want to *display* them oldest-first.
    //   The outer SELECT flips the order back to ASC.
    // =========================================================================
    @Override
    public List<Message> getRecent(int limit) throws SQLException {
        String sql =
            "SELECT id, username, content, sent_at " +
            "FROM (" +
            "  SELECT id, username, content, sent_at " +
            "  FROM messages " +
            "  ORDER BY sent_at DESC " +
            "  LIMIT ?" +
            ") sub " +
            "ORDER BY sent_at ASC";

        List<Message> result = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Message(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("content"),
                            rs.getTimestamp("sent_at").toLocalDateTime()
                    ));
                }
            }
        }
        return result;
    }
}
