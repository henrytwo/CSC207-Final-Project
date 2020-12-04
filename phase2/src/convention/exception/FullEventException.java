package convention.exception;

/**
 * Thrown when an events is unable to accept more attendees due to capacity limits
 */
public class FullEventException extends RuntimeException {
    public FullEventException() {
        super("Unable to register for events - Room is full.");
    }
}

