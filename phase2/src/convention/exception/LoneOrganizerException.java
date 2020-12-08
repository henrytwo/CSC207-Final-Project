package convention.exception;

/**
 * There cannot be zero organizers for a conference
 */
public class LoneOrganizerException extends RuntimeException {
    public LoneOrganizerException() {
        super("There must be at least one organizer for a conference.");
    }
}
