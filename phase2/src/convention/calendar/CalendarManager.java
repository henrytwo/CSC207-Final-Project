package convention.calendar;

import convention.exception.CalendarDoubleBookingException;
import convention.exception.NullBookingException;

import java.util.Map;
import java.util.UUID;

/**
 * Manages the Calendar entity
 */
public class CalendarManager {

    private Calendar calendar;

    public CalendarManager(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * Gets the full mapping from UUID to Time range
     *
     * @return
     */
    public Map<UUID, TimeRange> getUUIDtoTimeRanges() {
        return calendar.getUUIDToTimeRange();
    }

    /**
     * @param t TimeRange that is compared with existing TimeRange objects on this calendar
     * @return true iff t conflicts with an existing TimeRange in c
     */
    public boolean timeRangeOccupied(TimeRange t) {
        return calendar.hasConflict(t);
    }

    /**
     * adds the UUID and TimeRange of a event object to Calendar
     *
     * @param eventUUID the UUID of the event object
     * @param timeRange the TimeRange of the event object
     */
    public void addTimeBlock(UUID eventUUID, TimeRange timeRange) {
        if (this.timeRangeOccupied(timeRange)) {
            throw new CalendarDoubleBookingException();
        } else {
            calendar.addTimeBlock(eventUUID, timeRange);
        }
    }

    /**
     * Removes a booking from the calendar
     *
     * @param eventUUID the UUID of the event object
     */
    public void removeTimeBlock(UUID eventUUID) {
        if (calendar.getBooking(eventUUID) == null) {
            throw new NullBookingException(eventUUID);
        }

        calendar.removeTimeBlock(eventUUID);
    }
}
