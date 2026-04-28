package chatapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DB – Manages the single JDBC connection for the entire server process.
 *
 * Design pattern: Singleton
 *   - Only one DatabaseManager (and therefore one Connection) is ever created.
 *   - getInstance() is synchronized so it is safe when called from multiple threads.
 *
 * On first call, the constructor:
 *   1. Opens the JDBC connection
 *   2. Calls createTables() to ensure the schema exists
 *
 * HOW TO SET UP THE DATABASE
 * ---------------------------
 * Option A – MySQL (default, matches the other DB examples in this repo):
 *   1. Start MySQL and create a database:
 *        CREATE DATABASE chatapp_db;
 *   2. Update DB_URL, DB_USER, DB_PASS below.
 *
 * Option B – H2 in-memory (no installation needed, great for testing):
 *   DB_URL  = "jdbc:h2:mem:chatapp;DB_CLOSE_DELAY=-1"
 *   DB_USER = "sa"
 *   DB_PASS = ""
 *   Add h2-*.jar to the classpath.
 */
public class DatabaseManager {

    // ---- Change these constants to match your database ----
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/chatapp_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "password";

    // ---- Singleton state ----
    private static DatabaseManager instance;
    private        Connection       connection;

    // Private constructor – only called by getInstance()
    private DatabaseManager() throws SQLException {
        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        createTables();
    }

    /**
     * Returns the single shared instance (thread-safe via synchronized).
     * Re-creates the instance if the connection was closed.
     */
    public static synchronized DatabaseManager getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /** Returns the shared JDBC connection. */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Creates the two tables required by the chat app.
     * "IF NOT EXISTS" makes this safe to call on every startup.
     */
    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {

            // users – one row per unique username that has ever connected
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS users (" +
                "  id           INT AUTO_INCREMENT PRIMARY KEY," +
                "  username     VARCHAR(50)  NOT NULL UNIQUE," +
                "  connected_at DATETIME     DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );

            // messages – every chat message ever sent
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS messages (" +
                "  id       INT AUTO_INCREMENT PRIMARY KEY," +
                "  username VARCHAR(50) NOT NULL," +
                "  content  TEXT        NOT NULL," +
                "  sent_at  DATETIME    DEFAULT CURRENT_TIMESTAMP" +
                ")"
            );
        }
        System.out.println("[DB] Schema ready (tables: users, messages).");
    }

    /** Close the underlying JDBC connection. */
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
