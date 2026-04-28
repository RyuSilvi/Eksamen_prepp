package chatapp_tutorial.db;

import chatapp_tutorial.model.Message;

import java.sql.SQLException;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════
 * DB / OOP TUTORIAL – DAO Interface for Messages
 * ═══════════════════════════════════════════════════════════════════
 *
 * CONCEPT: Data Access Object (DAO) pattern
 *
 * The DAO pattern separates what the database can do (this interface)
 * from how it actually does it (MessageDAOImpl with real SQL).
 *
 * Benefits:
 *   - Easy to swap databases: change only the Impl class
 *   - Easy to write unit tests: create a "fake" Impl that returns
 *     test data without ever touching a real database
 *   - Clean separation of concerns (business logic vs. SQL)
 *
 * This file defines the CONTRACT.  MessageDAOImpl fulfils it.
 *
 * You do NOT need to change this file – it is already complete.
 * Study it and understand what each method does before implementing
 * MessageDAOImpl.
 * ═══════════════════════════════════════════════════════════════════
 */
public interface MessageDAO {

    /**
     * Persist a new message to the database.
     *
     * @param message  the message to save (id field ignored; DB auto-assigns it)
     * @throws SQLException if the INSERT fails
     */
    void save(Message message) throws SQLException;

    /**
     * Return the {@code limit} most recent messages, ordered oldest-first.
     *
     * @param limit  how many messages to retrieve
     * @return list of messages, oldest first (may be empty, never null)
     * @throws SQLException if the SELECT fails
     */
    List<Message> getRecent(int limit) throws SQLException;
}
