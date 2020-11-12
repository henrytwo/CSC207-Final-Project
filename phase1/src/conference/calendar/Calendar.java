package conference.calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class Calendar {
    // Look at the LocalDateTime library
    // You can probably just store pairs of LocalDateTime (for start and end), in a set (Maybe make another class for the pair?)
    // To check if a time slot is going to conflict, compare the new pair with the previous pairs
    // We'll probably also want to be able to store the event UUID in the pair
    //
    // Edit: The TimeRange class will actually handle the pair of times for you
    private Set<Pair<UUID, TimeRange>> eventUUIDTimeRangesPairs = new HashSet<>();
    
    /**
     * constructor for an empty calendar
     */
    public Calendar(){}
    
    /**
     * @return get a Set of UUID TimeRanges pairs booked on this calendar
     */
    public Set<Pair<UUID, TimeRange>> getEventUUIDTimeRangesPairs() {
        return eventUUIDTimeRangesPairs;
    }
    
    /**
     * @return get a Set of TimeRanges booked on this calendar
     */
    public Set<TimeRange> getTimeRanges() {
        Set<TimeRange> timeRanges = new HashSet<>();
        for ( Pair<UUID, TimeRange> eventTimeRangePair : this.eventUUIDTimeRangesPairs) {
            timeRanges.add(eventTimeRangePair.getTimeRange());
        }
        return timeRanges;
    }

    /**
     * @param t the TimeRange that is being compared to existing TimeRange objects on this calendar
     * @return true iff t conflicts with an existing TimeRange object
     */
    public boolean hasConflict(TimeRange t) {
        Set<TimeRange> timeRangeSet = this.getTimeRanges();
        for (TimeRange tr : timeRangeSet){
            if (tr.getStart().isEqual(t.getStart())) { return true; }
            else if (t.getStart().isAfter(tr.getStart()) & t.getStart().isBefore(tr.getEnd())) { return true; }
            else if (t.getEnd().isAfter(tr.getStart()) & t.getEnd().isBefore(tr.getEnd())) { return true; }
        }
        return false;
    }

    /**
     * add a Calendarable UUID, TimeRange pair to the set of all pairs for this calendar
     * @param p Pair for UUID of the calendarable object
     */
    public void bookCalendarable(Pair<UUID, TimeRange> p) {
        this.eventUUIDTimeRangesPairs.add(p);
    }
}
