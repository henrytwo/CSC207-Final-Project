package convention;

import convention.calendar.TimeRange;
import convention.conference.ConferenceManager;
import convention.event.EventManager;
import convention.exception.RoomInUseException;
import convention.permission.PermissionManager;
import convention.room.RoomManager;
import user.UserManager;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Operations on Rooms
 */
public class RoomController {

    private ConferenceManager conferenceManager;
    private PermissionManager permissionManager;

    /**
     * Constructs RoomController
     *
     * @param conferenceManager instance of conference manager
     * @param userManager       instance of user manager
     */
    public RoomController(ConferenceManager conferenceManager, UserManager userManager) {
        this.conferenceManager = conferenceManager;
        this.permissionManager = new PermissionManager(conferenceManager, userManager);
    }

    /**
     * Gets a set of UUIDs of rooms associated with this conference.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @return set of UUIDs of rooms associated with the conference
     */
    public Set<UUID> getRooms(UUID conferenceUUID, UUID executorUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);
        return conferenceManager.getRoomManager(conferenceUUID).getRooms();
    }

    /**
     * Create a new room for this conference.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomLocation   location of the room
     * @param roomCapacity   capacity of the room
     * @return UUID of the new room
     */
    public UUID createRoom(UUID conferenceUUID, UUID executorUUID, String roomLocation, int roomCapacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.createRoom(roomLocation, roomCapacity);
    }

    /**
     * Sets a new room location.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @param roomLocation   new room location
     */
    public void setRoomLocation(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, String roomLocation) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        roomManager.setRoomLocation(roomUUID, roomLocation);
    }

    /**
     * Sets a new room capacity.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @param roomCapacity   new room capacity
     */
    public void setRoomCapacity(UUID conferenceUUID, UUID executorUUID, UUID roomUUID, int roomCapacity) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        roomManager.setRoomCapacity(roomUUID, roomCapacity);
    }

    /**
     * Delete a room. This operation will be rejected if the room is currently used by one or more events.
     * <p>
     * Required Permission: ORGANIZER
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     */
    public void deleteRoom(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsOrganizer(conferenceUUID, executorUUID);

        EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        // TestView if this room is being used by any events
        for (UUID eventUUID : eventManager.getEvents()) {
            if (eventManager.getEventRoom(eventUUID).equals(roomUUID)) {
                throw new RoomInUseException(roomUUID, eventUUID);
            }
        }

        roomManager.deleteRoom(roomUUID);
    }

    /**
     * Gets a room's location.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @return room location
     */
    public String getRoomLocation(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.getRoomLocation(roomUUID);
    }

    /**
     * Gets a room's capacity.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @return room capacity
     */
    public int getRoomCapacity(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.getRoomCapacity(roomUUID);
    }

    /**
     * Get a map of events UUIDs and their respective time ranges for a specific room.
     * <p>
     * Required Permission: ATTENDEE
     *
     * @param conferenceUUID UUID of the conference to operate on
     * @param executorUUID   UUID of the user executing the command
     * @param roomUUID       UUID of the room to operate on
     * @return map of events UUIDs to their time range
     */
    public Map<UUID, TimeRange> getRoomSchedule(UUID conferenceUUID, UUID executorUUID, UUID roomUUID) {
        permissionManager.testIsAttendee(conferenceUUID, executorUUID);

        RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

        return roomManager.getCalendarManager(roomUUID).getUUIDtoTimeRanges();
    }
}
