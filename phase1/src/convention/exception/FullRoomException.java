package convention.exception;

public class FullRoomException extends RuntimeException {
    public FullRoomException() {
        super("Unable to register for event - Room is full.");
    }
}

