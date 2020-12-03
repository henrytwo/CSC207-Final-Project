package convention.exception;


/**
 * Throw this when an invalid sort method is provided
 * (sort method is not "day", "speaker", or "registered")
 */
public class InvalidSortMethodException extends RuntimeException{
    public InvalidSortMethodException() {
        super("Sort method must be one of \"day\", \"speaker\", or \"registered\". ");}
}
