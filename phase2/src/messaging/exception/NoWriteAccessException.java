package messaging.exception;

/**
 * raises an error if a user is not permitted to write messages from a conversation
 */
public class NoWriteAccessException extends RuntimeException {
    public NoWriteAccessException() {
        super("This user is not permitted to write messages from this conversation");
    }
}
