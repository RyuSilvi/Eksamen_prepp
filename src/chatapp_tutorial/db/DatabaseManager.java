package chatapp_tutorial.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ═══════════════════════════════════════════════════════════════════
 * DB TUTORIAL – DatabaseManager (Singleton pattern)
 * ═══════════════════════════════════════════════════════════════════
 *
 * CONCEPTS:
 *   - Singleton design pattern: only ONE instance of this class
 *     ever exists in the JVM.
 *   - JDBC connection management
 *   - Schema creation with CREATE TABLE IF NOT EXISTS
 *   - Thread-safe getInstance() using "synchronized"
 *
 * WHY SINGLETON FOR A DB CONNECTION?
 *   Opening a database connection is expensive (network handshake,
 *   authentication).  By reusing one connection object throughout
 *   the server's lifetime we avoid that overhead on every query.
 *
 * ─────────────────────────────────────────────────────────────────
 * HOW TO SET UP:
 *   Option A – MySQL:  run  CREATE DATABASE chatapp_db;
 *                      then update the three constants below.
 *   Option B – H2 (no install):
 *              DB_URL  = "jdbc:h2:mem:chatapp;DB_CLOSE_DELAY=-1"
 *              DB_USER = "sa"
 *              DB_PASS = ""
 *              (add h2-*.jar to classpath)
 * ═══════════════════════════════════════════════════════════════════
 */
public class DatabaseManager {

    // ---- Change these to match your database ----
    private static final String DB_URL  = "jdbc:mysql://localhost:3306/chatapp_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "password";

    // TODO 1: Declare the Singleton instance field.
    //         It must be:  private static DatabaseManager instance;
    //         (static = belongs to the class, not any object)
    //         (starts as null – created on first call to getInstance)



    // TODO 2: Declare the JDBC Connection field.
    //         private Connection connection;



    // ─────────────────────────────────────────────────────────────────────────
    // Private constructor – called ONLY by getInstance()
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 3: Implement the constructor.
     *   a) Open the connection:
     *        connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
     *   b) Call createTables() to set up the schema.
     *
     * The constructor is PRIVATE so no code outside this class can call
     * "new DatabaseManager()" – they must use getInstance() instead.
     */
    private DatabaseManager() throws SQLException {
        // TODO 3a: open connection

        // TODO 3b: call createTables()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // getInstance – the public entry point (Singleton pattern)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns the single shared DatabaseManager.
     * Creates it on the first call; returns the existing one on all later calls.
     *
     * "synchronized" ensures that if two threads call getInstance() at the
     * exact same moment, only ONE of them creates the instance.
     *
     * TODO 4: Implement the body:
     *   if (instance == null OR the connection is closed) {
     *       instance = new DatabaseManager();
     *   }
     *   return instance;
     *
     * HINT to check if connection is closed:  instance.connection.isClosed()
     */
    public static synchronized DatabaseManager getInstance() throws SQLException {
        // TODO 4
        return null; // replace with: return instance;
    }

    /** TODO 5: Return the connection field. */
    public Connection getConnection() {
        // TODO 5
        return null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // createTables – run once at startup
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Creates the two DB tables if they do not already exist.
     *
     * TODO 6: Use a try-with-resources Statement to execute two CREATE TABLE SQL statements.
     *
     * TABLE 1 – users:
     *   id           INT AUTO_INCREMENT PRIMARY KEY
     *   username     VARCHAR(50) NOT NULL UNIQUE
     *   connected_at DATETIME DEFAULT CURRENT_TIMESTAMP
     *
     * TABLE 2 – messages:
     *   id       INT AUTO_INCREMENT PRIMARY KEY
     *   username VARCHAR(50) NOT NULL
     *   content  TEXT NOT NULL
     *   sent_at  DATETIME DEFAULT CURRENT_TIMESTAMP
     *
     * HINT:
     *   try (Statement stmt = connection.createStatement()) {
     *       stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ( ... )");
     *       stmt.executeUpdate("CREATE TABLE IF NOT EXISTS messages ( ... )");
     *   }
     */
    private void createTables() throws SQLException {
        // TODO 6
    }

    /**
     * TODO 7: Close the connection (call connection.close() if it is not null and not already closed).
     */
    public void close() throws SQLException {
        // TODO 7
    }
}
