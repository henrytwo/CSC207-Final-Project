package contact.exception;

import java.util.UUID;

public class RequestDeniedException extends RuntimeException{
    public RequestDeniedException(UUID sender, UUID receiver) {
        super(String.format("Cannot send a request again. %s is already on the request list of %s.", sender, receiver));
    }
}
