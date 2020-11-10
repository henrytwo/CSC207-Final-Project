package util.exception;

public class RoomDoubleBookingException extends RuntimeException{
    public RoomDoubleBookingException() { super("The time slot given conflicts with an existing event. "); }
}

