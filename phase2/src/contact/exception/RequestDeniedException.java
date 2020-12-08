package contact.exception;

import java.util.UUID;

/**
 * raises an error if a request is repeated rapidly
 */
public class RequestDeniedException extends RuntimeException {
    public RequestDeniedException(UUID sender, UUID receiver) {
        super(String.format("Cannot send a request again. %s is already on the request list of %s.", sender, receiver));
    }
}
