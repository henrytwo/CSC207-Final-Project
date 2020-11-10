package conference.calendar;

import java.util.UUID;

public interface Calendarable {
    public TimeRange getTimeRange();
    public UUID getUUID();
}
