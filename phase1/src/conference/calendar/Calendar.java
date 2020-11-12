package conference.calendar;
import java.util.*;


public class Calendar {
    // Look at the LocalDateTime library
    // You can probably just store pairs of LocalDateTime (for start and end), in a set (Maybe make another class for the pair?)
    // To check if a time slot is going to conflict, compare the new pair with the previous pairs
    // We'll probably also want to be able to store the event UUID in the pair
    //
    // Edit: The TimeRange class will actually handle the pair of times for you
    private Map<UUID, TimeRange> uuidToTimeRange = new HashMap<>();
    
    /**
     * constructor for an empty calendar
     */
    public Calendar(){}

    public Map<UUID, TimeRange> getUUIDToTimeRange() {
        return uuidToTimeRange;
    }

    /**
     * @return get a Set of TimeRanges booked on this calendar
     */
    public Set<TimeRange> getUUIDtoTimeRanges() {
        return new HashSet<>(uuidToTimeRange.values());
    }

    /**
     * @param t the TimeRange that is being compared to existing TimeRange objects on this calendar
     * @return true iff t conflicts with an existing TimeRange object
     */
    public boolean hasConflict(TimeRange t) {
        Set<TimeRange> timeRangeSet = this.getUUIDtoTimeRanges();
        for (TimeRange tr : timeRangeSet){
            if (tr.getStart().isEqual(t.getStart())) { return true; }
            else if (t.getStart().isAfter(tr.getStart()) & t.getStart().isBefore(tr.getEnd())) { return true; }
            else if (t.getEnd().isAfter(tr.getStart()) & t.getEnd().isBefore(tr.getEnd())) { return true; }
        }
        return false;
    }

    /**
     * Books a time slot for an event in this calendar
     *
     * @param eventUUID UUID of the event
     * @param timeRange Time range of the event
     */
    public void addTimeBlock(UUID eventUUID, TimeRange timeRange) {
        this.uuidToTimeRange.put(eventUUID, timeRange);
    }
}
