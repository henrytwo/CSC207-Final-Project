package conference.room;

import conference.calendar.Calendar;

import java.util.*;

import conference.calendar.CalendarManager;
import util.exception.*;

public class RoomManager {

    private Map<UUID, Room> rooms;

    public RoomManager(Map<UUID, Room> rooms) {
        this.rooms = rooms;
    }

    public Set<UUID> getRooms() {
        return rooms.keySet();
    }

    public CalendarManager getCalendarManager(UUID roomUUID) {
        return new CalendarManager(getRoomCalendar(roomUUID));
    }

    public boolean roomExists(UUID roomUUID) {
        return rooms.containsKey(roomUUID);
    }

    private Room getRoom(UUID roomUUID) {
        if (!roomExists(roomUUID)) {
            throw new NullRoomException(roomUUID);
        }
        return rooms.get(roomUUID);
    }

    private Calendar getRoomCalendar(UUID roomUUID) {
        return getRoom(roomUUID).getCalendar();
    }

    public UUID createRoom(String roomLocation, int roomCapacity) {
        Room room = new Room(roomLocation, roomCapacity);// make the room here and stuff
        rooms.put(room.getUUID(), room);

        return room.getUUID();
    }

    public void setRoomLocation(UUID roomUUID, String roomLocation) {
        getRoom(roomUUID).setRoomLocation(roomLocation);
    }

    public void setRoomCapacity(UUID roomUUID, int capacity) {
        getRoom(roomUUID).setCapacity(capacity);
    }

    public void deleteRoom(UUID roomUUID) {
        if (!roomExists(roomUUID)) {
            throw new NullRoomException(roomUUID);
        }

        rooms.remove(roomUUID);
    }

    public String getRoomLocation(UUID roomUUID) {
        return getRoom(roomUUID).getRoomLocation();
    }

    public int getRoomCapacity(UUID roomUUID) {
        return getRoom(roomUUID).getCapacity();
    }

}
