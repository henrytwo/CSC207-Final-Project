package convention.exception;

/**
 * Thrown when an invalid room capacity is provided
 */
public class InvalidCapacityException extends RuntimeException {
    public InvalidCapacityException() {
        super("Room capacity must be greater than zero.");
    }
}
