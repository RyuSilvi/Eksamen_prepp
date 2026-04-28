package chatapp.db;

import chatapp.model.Message;

import java.sql.SQLException;
import java.util.List;

/**
 * DB / OOP – DAO interface for Message persistence.
 *
 * The DAO (Data Access Object) pattern separates database logic from
 * business logic.  This interface defines *what* can be done; the
 * implementation class (MessageDAOImpl) contains the actual SQL.
 *
 * Benefit: if you later switch from MySQL to PostgreSQL, you only
 * change the Impl class – nothing else in the application changes.
 */
public interface MessageDAO {

    /**
     * Persist a new message to the database.
     *
     * @param message  the message to save (id field is ignored; DB auto-assigns it)
     * @throws SQLException if the INSERT fails
     */
    void save(Message message) throws SQLException;

    /**
     * Retrieve the {@code limit} most recent messages, ordered oldest-first
     * (so they display naturally in the chat window).
     *
     * @param limit  maximum number of messages to return
     * @return list of messages (may be empty, never null)
     * @throws SQLException if the SELECT fails
     */
    List<Message> getRecent(int limit) throws SQLException;
}
