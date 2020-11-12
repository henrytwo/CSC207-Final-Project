package Exceptions;

public class UserNotPartOfChatException extends RuntimeException{
    public UserNotPartOfChatException(){
        super("User is not part of this Conversation, so not allowed to message in this Chat");
    }
}
