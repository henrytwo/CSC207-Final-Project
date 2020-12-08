package messaging.exception;

/**
 * raises an error if a user is not permitted to view a conversaiton
 */
public class NoReadAccessException extends RuntimeException {
    public NoReadAccessException() {
        super("This user is not permitted to read messages from this conversation");
    }
}
