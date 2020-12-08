package convention.exception;

/**
 * End time must be after Start time for time ranges
 */
public class InvalidTimeRangeException extends RuntimeException {
    public InvalidTimeRangeException() {
        super("The start time must be before the end time");
    }
}
