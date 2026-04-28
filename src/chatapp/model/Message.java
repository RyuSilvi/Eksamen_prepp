package chatapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * OOP – Model / entity class representing a single chat message.
 *
 * Demonstrates:
 *   - Encapsulation
 *   - Static constant (shared DateTimeFormatter)
 *   - toString() formatted for display in the chat window
 */
public class Message {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private int           id;
    private String        username;
    private String        content;
    private LocalDateTime sentAt;

    /** Full constructor – used when reading from the database. */
    public Message(int id, String username, String content, LocalDateTime sentAt) {
        this.id       = id;
        this.username = username;
        this.content  = content;
        this.sentAt   = sentAt;
    }

    /** Convenience constructor – used when a user sends a new message. */
    public Message(String username, String content) {
        this(0, username, content, LocalDateTime.now());
    }

    // ---- Getters ----
    public int           getId()       { return id; }
    public String        getUsername() { return username; }
    public String        getContent()  { return content; }
    public LocalDateTime getSentAt()   { return sentAt; }

    /** Returns a formatted string like: [14:32:07] Alice: Hello everyone! */
    @Override
    public String toString() {
        return String.format("[%s] %s: %s",
                sentAt.format(FORMATTER), username, content);
    }
}
