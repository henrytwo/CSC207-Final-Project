package convention.schedule;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Schedule {
    /**
     * Each sublist looks like in eventStringLists should look like
     * [event title, event time range, room location, name of speakers]
     */
    private List<List<String>> eventStringLists;
    private String title;

    public Schedule() {
        this.eventStringLists = new ArrayList<>();
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public String getTitle() {
        return this.title;
    }

    public void addEventStringList(List<String> s) {
        this.eventStringLists.add(s);
    }

    public List<List<String>> getEventStringLists() {
        return this.eventStringLists;
    }

}
