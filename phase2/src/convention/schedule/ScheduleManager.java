package convention.schedule;

import gateway.TablePrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ScheduleManager {
    Schedule schedule = new Schedule();

    /**
     * @param sortBy    one of "speaker", "registered", or "day"
     * @param titleInfo one of: speaker username, user username, a specified date
     */
    public void setScheduleTitle(String sortBy, String titleInfo) {
        if (sortBy.equals("speaker")) {
            schedule.setTitle("Events with speaker ".concat(titleInfo));
        } else if (sortBy.equals("registered")) {
            schedule.setTitle("Events ".concat(titleInfo).concat(" signed up for"));
        } else {
            schedule.setTitle("events on ".concat(titleInfo));
        }
    }

    public void addEventStringList(String title, String timeRange, String roomLocation, String speakers) {
        ArrayList<String> eventStringList = new ArrayList<>(
                Arrays.asList(title, timeRange, roomLocation, speakers)
        );
        this.schedule.addEventStringList(eventStringList);
    }

    public void print(String fileName) throws IOException {
        TablePrinter tablePrinter = new TablePrinter();
        tablePrinter.print(schedule.getTitle(), fileName);
    }
}
