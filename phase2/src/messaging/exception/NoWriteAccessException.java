package messaging.exception;

public class NoWriteAccessException extends RuntimeException {
    public NoWriteAccessException() {
        super("This user is not permitted to write messages from this conversation");
    }
}
