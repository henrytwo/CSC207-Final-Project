package convention.exception;

import java.util.UUID;

public class RoomInUseException extends RuntimeException {
    public RoomInUseException(UUID roomUUID, UUID eventUUID) {
        super(String.format("Room %s cannot be deleted as it is being by event %s", roomUUID, eventUUID));
    }
}
