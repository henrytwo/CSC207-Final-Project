package convention.exception;

public class InvalidEventTimeException extends RuntimeException {
    public InvalidEventTimeException() {
        super("Unable to book event at specified time since it goes beyond the start/end time of the parent conference.");
    }
}
