package convention.exception;

/**
 * Thrown when an invalid name is provided
 */
public class InvalidNameException extends RuntimeException {
    public InvalidNameException() {
        super("Name must be non-empty");
    }
}
