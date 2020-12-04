package convention.exception;

public class SpeakerDoubleBookingException extends RuntimeException {
    public SpeakerDoubleBookingException() {
        super("The speaker is already scheduled to be at another events at the given time.");
    }
}

