package convention.exception;

public class InvalidNameException extends RuntimeException {
    public InvalidNameException() {
        super("Name must be non-empty");
    }
}
