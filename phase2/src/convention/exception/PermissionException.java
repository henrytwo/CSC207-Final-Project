package convention.exception;

public class PermissionException extends RuntimeException {
    public PermissionException() {
        super("You don't have permission to perform this action.");
    }

    public PermissionException(String message) {
        super(String.format("You don't have permission to perform this action.\n%s", message));
    }
}
