package convention.schedule;

import convention.event.Event;

import java.io.IOException;
import java.util.Set;

public class ScheduleManager {

//    TODO: implement this
    public static Schedule constructSchedule(Set<Event> e, String sortBy) {
        Schedule s = new Schedule();

        return s;
    }


//    TODO: implement this
    public void print(Schedule s) throws IOException {
        util.SchedulePrinter.print(s);
    }
}
