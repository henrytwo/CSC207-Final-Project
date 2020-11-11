package conference.room;

import conference.calendar.Calendar;

import java.util.*;

import util.exception.*;

public class RoomManager {

    private Map<UUID, Room> rooms;

    public RoomManager(Map<UUID, Room> rooms) {
        this.rooms = rooms;
    }

    public Set<UUID> getRooms() {
        return rooms.keySet();
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

    public void createRoom(String roomLocation, int roomCapacity) {
        Room room = new Room(roomLocation, roomCapacity);// make the room here and stuff
        rooms.put(room.getUUID(), room);
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

    public Calendar getRoomCalendar(UUID roomUUID) {
        return getRoom(roomUUID).getCalendar();
    }
}
