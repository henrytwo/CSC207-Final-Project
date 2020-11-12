package util.exception;

import java.util.UUID;

public class NullRoomException extends RuntimeException {
    public NullRoomException(UUID roomUUID){
        super(String.format("Room %s does not exist.", roomUUID));
    }
}
