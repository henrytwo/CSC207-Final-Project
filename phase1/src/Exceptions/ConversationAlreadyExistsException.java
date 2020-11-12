package Exceptions;

public class ConversationAlreadyExistsException extends RuntimeException{
    public ConversationAlreadyExistsException(){
        super("Conversation Already Exists.");
    }
}
