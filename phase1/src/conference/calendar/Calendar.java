package conference.calendar;

public class Calendar {
    // Look at the LocalDateTime library
    // You can probably just store pairs of LocalDateTime (for start and end), in a set (Maybe make another class for the pair?)
    // To check if a time slot is going to conflict, compare the new pair with the previous pairs
    // We'll probably also want to be able to store the event UUID in the pair
    //
    // Edit: The TimeRange class will actually handle the pair of times for you
}
