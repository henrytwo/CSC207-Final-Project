package convention.exception;

/**
 * Thrown when an event is unable to accept more attendees due to capacity limits
 */
public class FullEventException extends RuntimeException {
    public FullEventException() {
        super("Unable to register for event - Room is full.");
    }
}

