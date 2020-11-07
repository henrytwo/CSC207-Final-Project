package util;

public class InvalidTimeRangeException extends RuntimeException {
    public InvalidTimeRangeException() {
        super("The start time must be before the end time");
    }
}
