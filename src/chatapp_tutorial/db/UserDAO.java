package chatapp_tutorial.db;

import chatapp_tutorial.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * ═══════════════════════════════════════════════════════════════════
 * DB / OOP TUTORIAL – DAO Interface for Users
 * ═══════════════════════════════════════════════════════════════════
 *
 * Same DAO pattern as MessageDAO.  No changes needed here.
 * Study this before implementing UserDAOImpl.
 * ═══════════════════════════════════════════════════════════════════
 */
public interface UserDAO {

    /**
     * Register a user when they connect for the first time.
     * If the username already exists, this is silently ignored.
     *
     * @param user  user to register
     * @throws SQLException on unexpected DB error
     */
    void save(User user) throws SQLException;

    /**
     * Return every username that has ever connected, ordered by first connection time.
     *
     * @return list of users (may be empty, never null)
     * @throws SQLException on DB error
     */
    List<User> findAll() throws SQLException;
}
