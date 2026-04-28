package chatapp.model;

import java.time.LocalDateTime;

/**
 * OOP – Model / entity class representing a chat user.
 *
 * Demonstrates:
 *   - Encapsulation: all fields are private; access via public getters
 *   - Multiple constructors (convenience constructor vs full constructor)
 *   - toString() override for readable logging
 */
public class User {

    private int           id;
    private String        username;
    private LocalDateTime connectedAt;

    /** Full constructor – used when reading from the database. */
    public User(int id, String username, LocalDateTime connectedAt) {
        this.id          = id;
        this.username    = username;
        this.connectedAt = connectedAt;
    }

    /** Convenience constructor – used when a new user connects (id not yet known). */
    public User(String username) {
        this(0, username, LocalDateTime.now());
    }

    // ---- Getters ----
    public int           getId()          { return id; }
    public void          setId(int id)    { this.id = id; }
    public String        getUsername()    { return username; }
    public LocalDateTime getConnectedAt() { return connectedAt; }

    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', connectedAt=%s}",
                id, username, connectedAt);
    }
}
