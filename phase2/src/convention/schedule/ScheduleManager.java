package convention.schedule;

import convention.event.Event;
import gateway.SchedulePrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScheduleManager {

    /**
     * @param eventStringListsList list of list of strings, each sublist looks like
     *                             [event title,
     *                             event time range,
     *                             room location,
     *                             name of speakers]
     * @param sortBy one of "speaker", "registered", or "day"
     * @param titleInfo one of: speaker username, user username, a specified date
     * @return a Schedule with given information
     */
    public static Schedule constructSchedule(List<List<String>> eventStringListsList, String sortBy, String titleInfo) {
        Schedule s = new Schedule();
        if (sortBy.equals("speaker")) {
            s.setTitle("Events with speaker ".concat(titleInfo));
        }
        else if (sortBy.equals("registered")) {
            s.setTitle("Events ".concat(titleInfo).concat(" signed up for"));
        }
        else {s.setTitle("events on ".concat(titleInfo));}
        s.setEventStringList(eventStringListsList);
        return s;
    }

    public static void print(Schedule s) throws IOException {
        SchedulePrinter.print(s.getEventStringLists(), s.getTitle());
    }
}
