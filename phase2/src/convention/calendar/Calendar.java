package convention.calendar;

import java.io.Serializable;
import java.util.*;

/**
 * Calendar object - stores mappings from events UUID to timeRange and prevents conflicts
 */
public class Calendar implements Serializable {
    // Look at the LocalDateTime library
    // You can probably just store pairs of LocalDateTime (for start and end), in a set (Maybe make another class for the pair?)
    // To check if a time slot is going to conflict, compare the new pair with the previous pairs
    // We'll probably also want to be able to store the events UUID in the pair
    //
    // Edit: The TimeRange class will actually handle the pair of times for you
    private final Map<UUID, TimeRange> uuidToTimeRange = new HashMap<>();

    /**
     * Gets the full mapping from UUID to Time range
     *
     * @return a map of UUIDs to timeRange
     */
    Map<UUID, TimeRange> getUUIDToTimeRange() {
        return new HashMap<>(uuidToTimeRange);
    }

    /**
     * @return get a Set of TimeRanges booked on this calendar
     */
    private Set<TimeRange> getUUIDtoTimeRanges() {
        return new HashSet<>(uuidToTimeRange.values());
    }

    /**
     * @param t the TimeRange that is being compared to existing TimeRange objects on this calendar
     * @return true iff t conflicts with an existing TimeRange object
     */
    boolean hasConflict(TimeRange t) {
        Set<TimeRange> timeRangeSet = this.getUUIDtoTimeRanges();
        for (TimeRange tr : timeRangeSet) {
            if (t.hasOverlap(tr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Books a time slot for an events in this calendar
     *
     * @param eventUUID UUID of the events
     * @param timeRange Time range of the events
     */
    void addTimeBlock(UUID eventUUID, TimeRange timeRange) {
        this.uuidToTimeRange.put(eventUUID, timeRange);
    }

    /**
     * Frees a time slot for an events in this calendar
     *
     * @param eventUUID UUID of the event
     */
    void removeTimeBlock(UUID eventUUID) {
        this.uuidToTimeRange.remove(eventUUID);
    }

    /**
     * Gets a time range for a booking
     *
     * @param eventUUID UUID of the events
     */
    TimeRange getBooking(UUID eventUUID) {
        return this.uuidToTimeRange.get(eventUUID);
    }
}
