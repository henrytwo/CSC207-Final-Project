package exception;

public class NoReadAccessException extends RuntimeException{
    public NoReadAccessException(){
        super("This user is not permitted to read messages from this conversation");
    }
}
