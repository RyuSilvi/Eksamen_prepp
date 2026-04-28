package chatapp_tutorial.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ═══════════════════════════════════════════════════════════════════
 * OOP TUTORIAL – Model class for a chat Message
 * ═══════════════════════════════════════════════════════════════════
 *
 * CONCEPTS:
 *   - Encapsulation (private fields, public getters)
 *   - Static constants (shared across ALL instances)
 *   - toString() formatting
 * ═══════════════════════════════════════════════════════════════════
 */
public class Message {

    /**
     * Static constant – shared by ALL Message objects.
     * Formats a LocalDateTime as "HH:mm:ss" (e.g. "14:32:07").
     *
     * "static final" = one copy for the whole class, never changes.
     */
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    // TODO 1: Declare FOUR private fields:
    //         - int            id
    //         - String         username
    //         - String         content
    //         - LocalDateTime  sentAt



    // ─────────────────────────────────────────────────────────────────────────
    // CONSTRUCTORS
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Full constructor – used when reading messages back from the DB.
     *
     * TODO 2: Assign all four parameters to their fields.
     */
    public Message(int id, String username, String content, LocalDateTime sentAt) {
        // TODO 2
    }

    /**
     * Convenience constructor – used when a user sends a new message.
     * id = 0 (DB not yet called), sentAt = now.
     *
     * TODO 3: Delegate to the full constructor.
     * HINT: this(0, username, content, LocalDateTime.now());
     */
    public Message(String username, String content) {
        // TODO 3
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GETTERS
    // ─────────────────────────────────────────────────────────────────────────

    /** TODO 4: Return id. */
    public int getId() {
        // TODO 4
        return 0;
    }

    /** TODO 5: Return username. */
    public String getUsername() {
        // TODO 5
        return null;
    }

    /** TODO 6: Return content. */
    public String getContent() {
        // TODO 6
        return null;
    }

    /** TODO 7: Return sentAt. */
    public LocalDateTime getSentAt() {
        // TODO 7
        return null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // toString
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * TODO 8: Return a formatted string like:
     *   [14:32:07] Alice: Hello everyone!
     *
     * HINT: Use String.format("[%s] %s: %s", sentAt.format(FORMATTER), username, content)
     */
    @Override
    public String toString() {
        // TODO 8
        return "";
    }
}
