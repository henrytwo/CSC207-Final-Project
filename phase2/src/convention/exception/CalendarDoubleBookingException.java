package convention.exception;

/**
 * Thrown when a calendar attempts to be double booked
 */
public class CalendarDoubleBookingException extends RuntimeException {
    public CalendarDoubleBookingException() {
        super("The time slot given conflicts with an existing event in the same room.");
    }
}

