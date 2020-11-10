package conference.calendar;

import javafx.util.Pair;
import util.exception.RoomDoubleBookingException;

import java.util.UUID;

public class CalendarManager {


    /**
     * @param t TimeRange that is compared with existing TimeRange objects on this calendar
     * @param c the Calendar that is being checked
     * @return true iff t conflicts with an existing TimeRange in c
     */
    public boolean timeRangeOccupied (TimeRange t, Calendar c) {
        return c.hasConflict(t);
    }

    /**
     * adds the UUID and TimeRange of a Calendarable object to Calendar
     * @param id
     * @param t
     * @param c
     */
    public void addTimeBlock (UUID id, TimeRange t, Calendar c) {
        if (this.timeRangeOccupied(t, c)) { throw new RoomDoubleBookingException(); }
        else {
            Pair<UUID, TimeRange> p = new Pair<>(id, t);
            c.bookCalendarable(p);
        }

    }
}
