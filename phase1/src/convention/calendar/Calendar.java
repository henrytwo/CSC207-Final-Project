package convention.calendar;

import java.io.Serializable;
import java.util.*;

/**
 * Calendar object - stores mappings from event UUID to timerange and prevents conflicts
 */
public class Calendar implements Serializable {
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
    public Calendar() {
    }

    /**
     * Gets the full mapping from UUID to Time range
     *
     * @return
     */
    public Map<UUID, TimeRange> getUUIDToTimeRange() {
        return new HashMap<>(uuidToTimeRange);
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
        for (TimeRange tr : timeRangeSet) {
            if (t.hasOverlap(tr)) {
                return true;
            }
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

    /**
     * Frees a time slot for an event in this calendar
     *
     * @param eventUUID UUID of the eventt
     */
    public void removeTimeBlock(UUID eventUUID) {
        this.uuidToTimeRange.remove(eventUUID);
    }

    /**
     * Gets a time range for a booking
     *
     * @param eventUUID UUID of the event
     */
    public TimeRange getBooking(UUID eventUUID) {
        return this.uuidToTimeRange.get(eventUUID);
    }
}
