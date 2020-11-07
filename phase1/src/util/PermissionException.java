package util;

public class PermissionException extends RuntimeException {
    public PermissionException() {
        super("You don't have permission to perform this action.");
    }
}
