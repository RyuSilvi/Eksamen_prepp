package chatapp.db;

import chatapp.model.User;

import java.sql.SQLException;
import java.util.List;

/**
 * DB / OOP – DAO interface for User persistence.
 *
 * Follows the same DAO pattern as MessageDAO:
 *   interface (UserDAO) + implementation (UserDAOImpl).
 */
public interface UserDAO {

    /**
     * Register a user the first time they connect.
     * If the username already exists, the operation is silently ignored.
     *
     * @param user  user to register
     * @throws SQLException if the INSERT fails unexpectedly
     */
    void save(User user) throws SQLException;

    /**
     * Return every username that has ever connected, ordered by first connection time.
     *
     * @return list of users (may be empty, never null)
     * @throws SQLException if the SELECT fails
     */
    List<User> findAll() throws SQLException;
}
