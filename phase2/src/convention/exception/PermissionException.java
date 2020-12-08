package convention.exception;

/**
 * Thrown when the provided executor UUID corresponds to a user that has insufficient permissions to perform an action
 */
public class PermissionException extends RuntimeException {
    public PermissionException(String message) {
        super(String.format("You don't have permission to perform this action.\n%s", message));
    }
}
