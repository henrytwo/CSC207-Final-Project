package convention.schedule;

import gateway.TablePrinter;
import java.io.IOException;
import java.util.List;

public class ScheduleManager {
    Schedule schedule = new Schedule();

    /**
     * @param eventStringListsList list of list of strings, each sublist looks like
     *                             [event title,
     *                             event time range,
     *                             room location,
     *                             name of speakers]
     * @param sortBy               one of "speaker", "registered", or "day"
     * @param titleInfo            one of: speaker username, user username, a specified date
     */
    public void constructSchedule(List<List<String>> eventStringListsList, String sortBy, String titleInfo) {
        if (sortBy.equals("speaker")) {
            schedule.setTitle("Events with speaker ".concat(titleInfo));
        } else if (sortBy.equals("registered")) {
            schedule.setTitle("Events ".concat(titleInfo).concat(" signed up for"));
        } else {
            schedule.setTitle("events on ".concat(titleInfo));
        }
        schedule.setEventStringList(eventStringListsList);
    }

    public void print() throws IOException {
        TablePrinter tablePrinter = new TablePrinter();
        tablePrinter.print(schedule.getTitle());
    }
}
