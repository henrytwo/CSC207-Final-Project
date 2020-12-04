package convention.schedule;

import java.util.ArrayList;

public class Schedule {
    private ArrayList<ArrayList<String>> eventStringLists;
    private String title;

    public Schedule() {
        this.eventStringLists = new ArrayList<>();
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public String getTitle() {return this.title;}

    public void setEventStringList(ArrayList<ArrayList<String>> s) {
        this.eventStringLists = s;
    }

    public ArrayList<ArrayList<String>> getEventStringLists() { return this.eventStringLists;}

}
