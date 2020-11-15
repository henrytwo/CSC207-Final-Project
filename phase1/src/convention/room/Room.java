package convention.room;

import convention.calendar.Calendar;

import java.io.Serializable;
import java.util.*;

/**
 * Room entity
 */
public class Room implements Serializable {

    //unique ID for the room
    private UUID roomUUID;
    //stores capacity of the room
    private int capacity;
    //store calendar for this room, calendar stores the times and event IDs which are in this room
    private Calendar calendar;
    //location as a string
    private String roomLocation;

    public Room(String roomLocation, int capacity) {
        this.roomUUID = UUID.randomUUID();
        this.capacity = capacity;
        this.roomLocation = roomLocation;
        this.calendar = new Calendar();
    }

    public UUID getUUID() {
        return this.roomUUID;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public String getRoomLocation() {
        return this.roomLocation;
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public void setCapacity(int newCapacity) {
        this.capacity = newCapacity;
    }

    public void setRoomLocation(String newLocation) {
        this.roomLocation = newLocation;
    }
}
