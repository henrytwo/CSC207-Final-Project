package convention.exception;

public class NegativeCapacityException extends RuntimeException {
    public NegativeCapacityException() {
        super("Room capacity must be a positive number.");
    }
}
