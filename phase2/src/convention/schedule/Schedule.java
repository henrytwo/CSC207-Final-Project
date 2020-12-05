package convention.schedule;

import java.util.ArrayList;
import java.util.List;

public class Schedule {
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

    public void setEventStringList(List<List<String>> s) {
        this.eventStringLists = s;
    }

    public List<List<String>> getEventStringLists() {
        return this.eventStringLists;
    }

}
