package conference.room;

import conference.calendar.Calendar;

import java.util.*;

import util.exception.*;

public class RoomManager {

    public boolean roomExists(Map<UUID, Room> rooms, UUID roomUUID) {
        return rooms.containsKey(roomUUID);
    }

    private Room getRoom(Map<UUID, Room> rooms, UUID roomUUID) {
        if (!roomExists(rooms, roomUUID)) {
            throw new NullRoomException(roomUUID);
        }
        return rooms.get(roomUUID);

    }

    public void createRoom(Map<UUID, Room> rooms, String roomLocation, int roomCapacity) {
        Room room = new Room(roomLocation, roomCapacity);// make the room here and stuff
        rooms.put(room.getUUID(), room);
    }

    public void setRoomLocation(Map<UUID, Room> rooms, UUID roomUUID, String roomLocation) {
        getRoom(rooms, roomUUID).setRoomLocation(roomLocation);
    }

    public void setRoomCapacity(Map<UUID, Room> rooms, UUID roomUUID, int capacity) {
        getRoom(rooms, roomUUID).setCapacity(capacity);
    }

    public void deleteRoom(Map<UUID, Room> rooms, UUID room) {
        try {
            getRoom(rooms, room);
        } catch (NullRoomException e) {
            System.out.printf("Room %s does not exist.\n", room);
        }
        rooms.remove(room);
    }

    public String getRoomLocation(Map<UUID, Room> rooms, UUID roomUUID) {
        return getRoom(rooms, roomUUID).getRoomLocation();
    }

    public int getRoomCapacity(Map<UUID, Room> rooms, UUID roomUUID) {
        return getRoom(rooms, roomUUID).getCapacity();
    }

    public Calendar getRoomCalendar(Map<UUID, Room> rooms, UUID roomUUID) {
        return getRoom(rooms, roomUUID).getCalendar();
    }
}
