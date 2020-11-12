package convention.exception;

public class CalendarDoubleBookingException extends RuntimeException{
    public CalendarDoubleBookingException() { super("The time slot given conflicts with an existing event. "); }
}

