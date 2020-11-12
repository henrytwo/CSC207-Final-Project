package convention.exception;

public class DoubleBookingException extends RuntimeException{
    public DoubleBookingException() { super("The time slot given conflicts with an existing event. "); }
}

