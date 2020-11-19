package convention.exception;

public class SpeakerDoubleBookingException extends RuntimeException {
    public SpeakerDoubleBookingException() {
        super("The speaker is already scheduled to be at another event at the given time.");
    }
}

