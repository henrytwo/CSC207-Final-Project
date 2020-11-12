package convention.calendar;

import convention.exception.DoubleBookingException;

import java.util.Map;
import java.util.UUID;

public class CalendarManager {

    private Calendar calendar;

    public CalendarManager(Calendar calendar) {
        this.calendar = calendar;
    }

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
            throw new DoubleBookingException();
        } else {
            calendar.addTimeBlock(eventUUID, timeRange);
        }
    }
}
