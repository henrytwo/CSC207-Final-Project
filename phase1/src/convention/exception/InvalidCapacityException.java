package convention.exception;

public class InvalidCapacityException extends RuntimeException {
    public InvalidCapacityException() {
        super("Room capacity  must be greater than zero.");
    }
}
