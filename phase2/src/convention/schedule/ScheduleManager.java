package convention.schedule;

import convention.event.Event;
import gateway.SchedulePrinter;

import java.io.IOException;
import java.util.Set;

public class ScheduleManager {

//    TODO: implement this
    public static Schedule constructSchedule(Set<Event> e, String sortBy) {
        Schedule s = new Schedule();

        return s;
    }


    public void print(Schedule s) throws IOException {
        SchedulePrinter.print(s.getEventStringLists(), s.getTitle());
    }
}
