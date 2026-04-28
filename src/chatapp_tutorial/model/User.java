package chatapp_tutorial.model;

import java.time.LocalDateTime;

/**
 * ═══════════════════════════════════════════════════════════════════
 * OOP TUTORIAL – Model class for a chat User
 * ═══════════════════════════════════════════════════════════════════
 *
 * CONCEPT: Encapsulation
 *   All fields are PRIVATE – they can only be accessed through the
 *   public methods (getters/setters).  This protects the internal
 *   state of the object.
 *
 * TASK: Fill in the missing parts marked with TODO.
 * ═══════════════════════════════════════════════════════════════════
 */
public class User {

    // TODO 1: Declare THREE private fields:
    //         - int       id
    //         - String    username
    //         - LocalDateTime connectedAt
    //
    // HINT: private <type> <fieldName>;



    // ─────────────────────────────────────────────────────────────────────────
    // CONSTRUCTORS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Full constructor – used when reading a User back from the database.
     *
     * TODO 2: Assign each parameter to the matching field using "this.fieldName = parameter".
     */
    public User(int id, String username, LocalDateTime connectedAt) {
        // TODO 2: this.id = id;  (do the same for the other two fields)

    }

    /**
     * Convenience constructor – used when a brand-new user connects.
     * The id is 0 (unknown until the DB assigns one) and connectedAt is now.
     *
     * TODO 3: Call the full constructor above using this(...).
     *         Pass: id=0, the given username, LocalDateTime.now()
     *
     * HINT: this(0, username, LocalDateTime.now());
     */
    public User(String username) {
        // TODO 3

    }

    // ─────────────────────────────────────────────────────────────────────────
    // GETTERS  (return the value of a private field)
    // ─────────────────────────────────────────────────────────────────────────

    /** TODO 4: Return the id field. */
    public int getId() {
        // TODO 4
        return 0; // replace this placeholder
    }

    /** TODO 5: Allow the id to be set after the DB assigns it. */
    public void setId(int id) {
        // TODO 5
    }

    /** TODO 6: Return the username field. */
    public String getUsername() {
        // TODO 6
        return null; // replace this placeholder
    }

    /** TODO 7: Return the connectedAt field. */
    public LocalDateTime getConnectedAt() {
        // TODO 7
        return null; // replace this placeholder
    }

    // ─────────────────────────────────────────────────────────────────────────
    // toString – OOP best practice: always override toString for model classes
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 8: Override toString() to return a readable description of this User.
     *
     * Expected format:  User{id=1, username='Alice', connectedAt=2024-01-15T10:30:00}
     *
     * HINT: Use String.format("User{id=%d, username='%s', connectedAt=%s}", ...)
     */
    @Override
    public String toString() {
        // TODO 8
        return "";
    }
}
