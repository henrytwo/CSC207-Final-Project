package convention.exception;

import java.util.UUID;

/**
 * Thrown when a room is attempted to be deleted while coupled to an event
 */
public class RoomInUseException extends RuntimeException {
    public RoomInUseException(UUID roomUUID, UUID eventUUID) {
        super(String.format("Room %s cannot be deleted as it is being used by event %s", roomUUID, eventUUID));
    }
}
