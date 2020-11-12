package exception;

public class ConversationAlreadyExistsException extends RuntimeException{
    public ConversationAlreadyExistsException(){
        super("Conversation Already Exists.");
    }
}
