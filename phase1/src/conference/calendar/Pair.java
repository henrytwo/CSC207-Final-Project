package conference.calendar;

import java.util.UUID;

public class Pair<U, T> {
    private TimeRange t;
    private UUID id;

    public Pair(UUID id, TimeRange t) {
        this.t = t;
        this.id = id;
    }

    public TimeRange getTimeRange() { return this.t; }

    public UUID getId() { return this.id;}
}
