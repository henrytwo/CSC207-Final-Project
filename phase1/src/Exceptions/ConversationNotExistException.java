package Exceptions;

public class ConversationNotExistException extends RuntimeException{
    public ConversationNotExistException(){
        super("This conversation does not exist in the system");
    }
}
