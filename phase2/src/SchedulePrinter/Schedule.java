package SchedulePrinter;

import java.util.Set;

public class Schedule {
    private Set<Set<String>> eventStringSets;
    private String title;

    public Schedule() {}

    public void setTitle(String t) {
        this.title = t;
    }

//TODO: implement this
    public void addEventStringSet(Set<String> s) {}

//    TODO: actual implementation
    @Override
    public String toString() {
        return "Schedule{" +
                "eventStringSets=" + eventStringSets +
                ", title='" + title + '\'' +
                '}';
    }
}
