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

    /**
     * Room constructor.
     *
     * @param roomLocation Location of the room in predefined format
     * @param capacity capacity of this room
     */
    public Room(String roomLocation, int capacity) {
        this.roomUUID = UUID.randomUUID();
        this.capacity = capacity;
        this.roomLocation = roomLocation;
        this.calendar = new Calendar();
    }

    /**
     * Gets the UUID of the Room
     *
     * @return the UUID of the given Room object
     */
    public UUID getUUID() {
        return this.roomUUID;
    }

    /**
     * Gets the capacity of the Room
     *
     * @return the capacity of this Room
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Gets the location of the Room
     *
     * @return the String location of the Room
     */
    public String getRoomLocation() {
        return this.roomLocation;
    }

    /**
     * Gets the calendar associated with this Room
     *
     * @return the Calendar object for this Room
     */
    public Calendar getCalendar() {
        return this.calendar;
    }

    /**
     * Changes the capacity of this Room
     *
     * @param newCapacity the new capacity of this Room
     */
    public void setCapacity(int newCapacity) {
        this.capacity = newCapacity;
    }

    /**
     * Changes the location of this Room
     *
     * @param newLocation the new location for this Room
     */
    public void setRoomLocation(String newLocation) {
        this.roomLocation = newLocation;
    }
}
