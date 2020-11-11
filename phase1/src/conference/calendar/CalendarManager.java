package conference.calendar;

import util.exception.DoubleBookingException;

import java.util.Set;
import java.util.UUID;

public class CalendarManager {

    private Calendar calendar;

    public CalendarManager(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * @param t TimeRange that is compared with existing TimeRange objects on this calendar
     * @return true iff t conflicts with an existing TimeRange in c
     */
    public boolean timeRangeOccupied(TimeRange t) {
        return calendar.hasConflict(t);
    }

    /**
     * adds the UUID and TimeRange of a Calendarable object to Calendar
     *
     * @param calendarable the calendarable object
     */
    public void addTimeBlock(Calendarable calendarable) {
        if (this.timeRangeOccupied(calendarable.getTimeRange())) {
            throw new DoubleBookingException();
        } else {
            Pair<UUID, TimeRange> p = new Pair<>(calendarable.getUUID(), calendarable.getTimeRange());
            calendar.bookCalendarable(p);
        }
    }
}
