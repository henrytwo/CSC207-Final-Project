import conference.ConferenceController;
import conference.calendar.TimeRange;
import org.junit.*;
import util.exception.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class ConferenceTest {

    /* Setup tests */
    UUID myUser = UUID.randomUUID();
    UUID randomUser = UUID.randomUUID();
    UUID someOrganizer = UUID.randomUUID();
    UUID someAttendee = UUID.randomUUID();
    UUID someSpeaker = UUID.randomUUID();
    UUID randomConference = UUID.randomUUID();

    String emptyString = "";
    String conferenceNameA = "Conference A";
    String conferenceNameB = "Conference B";

    LocalDateTime dateA = LocalDateTime.of(2015,
            Month.JULY, 29, 19, 30, 40);
    LocalDateTime dateB = LocalDateTime.of(2018,
            Month.JULY, 29, 19, 30, 40);

    TimeRange timeRangeA = new TimeRange(dateA, dateB);

    LocalDateTime dateC = LocalDateTime.of(2020,
            Month.APRIL, 2, 1, 3, 20);
    LocalDateTime dateD = LocalDateTime.of(2029,
            Month.AUGUST, 29, 19, 30, 40);

    TimeRange timeRangeB = new TimeRange(dateC, dateD);

    ConferenceController conferenceController;

    @Before
    public void init() {
        conferenceController = new ConferenceController();
    }

    // Test with and without permission

    // Admin tasks

    // Create conf X
    // Edit conf
        // Add organizer X
        // Remove organizer X
        // Remove attendee
        // Edit date X
        // Edit name X
    // Delete conf X

    // Create event
        // Assign a speaker
    // Edit event
    // Delete event
        // Revoke speaker access if they don't have any more events

    // Create room
    // Edit room
    // Delete room

    // User tasks

    // Join conference
    //    - Invite? Search?
    // Register in event
    // List events
    //    - Test the different ways to sort

    // Speaker tasks

    // List events they're running
    // Create convo for an event

    /* Conference creation */

    /**
     * The conference should exist after it is created... duh
     */
    @Test(timeout = 50)
    public void testCreateConference() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        assertTrue(conferenceController.conferenceExists(conferenceUUID));
        assertEquals(conferenceController.getConferenceName(conferenceUUID), conferenceNameA);
        assertEquals(conferenceController.getConferenceTimeRange(conferenceUUID), timeRangeA);
        assertTrue(conferenceController.getOrganizers(conferenceUUID, myUser).contains(myUser));
    }

    /**
     * You need a name...
     */
    @Test(timeout = 50, expected = InvalidNameException.class)
    public void testCreateConferenceInvalidName() {
        conferenceController.createConference(emptyString, timeRangeA, myUser);
    }

    /* Editing a conference */
    @Test(timeout = 50)
    public void testEditConferenceName() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceName(conferenceUUID, myUser, conferenceNameB);
        assertEquals(conferenceController.getConferenceName(conferenceUUID), conferenceNameB);
    }

    /**
     * You need a name...
     */
    @Test(timeout = 50, expected = InvalidNameException.class)
    public void testEditConferenceNameInvalidName() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceName(conferenceUUID, myUser, emptyString);
    }

    /**
     * You must be an organizer
     */
    @Test(timeout = 50, expected = PermissionException.class)
    public void testEditConferenceNameInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceName(conferenceUUID, randomUser, conferenceNameB);
    }

    @Test(timeout = 50)
    public void testEditConferenceDates() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceTimeRange(conferenceUUID, myUser, timeRangeB);

        assertEquals(conferenceController.getConferenceTimeRange(conferenceUUID), timeRangeB);
    }

    /**
     * You must be an organizer
     */
    @Test(timeout = 50, expected = PermissionException.class)
    public void testEditConferenceDatesInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceTimeRange(conferenceUUID, randomUser, timeRangeB);
    }

    /* Deleting a conference */
    @Test(timeout = 50)
    public void testDeleteConference() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        conferenceController.deleteConference(conferenceUUID, myUser);
        assertFalse(conferenceController.conferenceExists(conferenceUUID));
    }

    /**
     * You can't delete a conference that doesn't exist
     */
    @Test(timeout = 50, expected = NullConferenceException.class)
    public void testDeleteInvalidConference() {
        conferenceController.deleteConference(randomConference, myUser);
    }

    /**
     * Only organizers can do this
     */
    @Test(timeout = 50, expected = PermissionException.class)
    public void testDeleteConferenceInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        conferenceController.deleteConference(conferenceUUID, randomUser);
    }

    /* Managing organizers */
    @Test(timeout = 50)
    public void testAddOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        // Test that there is only the initial user
        Set<UUID> organizers = conferenceController.getOrganizers(conferenceUUID, myUser);
        assertTrue(organizers.size() == 1 && organizers.contains(myUser));

        // Add the new organizer
        conferenceController.addOrganizer(conferenceUUID, myUser, someOrganizer);

        // Ensure new organizer has been added successfully
        assertTrue(organizers.size() == 2 && organizers.contains(myUser) && organizers.contains(someOrganizer));
    }

    /**
     * Only organizers can do this
     */
    @Test(timeout = 50, expected = PermissionException.class)
    public void testAddOrganizerInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.addOrganizer(conferenceUUID, randomUser, someOrganizer);
    }

    @Test(timeout = 50)
    public void testRemoveOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        // Add the new organizer
        conferenceController.addOrganizer(conferenceUUID, myUser, someOrganizer);

        // Test that both users have been added
        Set<UUID> organizers = conferenceController.getOrganizers(conferenceUUID, myUser);
        assertTrue(organizers.size() == 2 && organizers.contains(myUser) && organizers.contains(someOrganizer));

        // Remove the organizer
        conferenceController.removeOrganizer(conferenceUUID, myUser, someOrganizer);

        // Test that the organizer has indeed been removed
        assertTrue(organizers.size() == 1 && organizers.contains(myUser) && !organizers.contains(someOrganizer));
    }

    /**
     * You can't remove yourself if you're the last organizer
     */
    @Test(timeout = 50, expected = LoneOrganizerException.class)
    public void testRemoveLastOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        // Remove the organizer
        conferenceController.removeOrganizer(conferenceUUID, myUser, myUser);
    }

    /**
     * Only organizers can do this
     */
    @Test(timeout = 50, expected = PermissionException.class)
    public void testRemoveOrganizerInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.removeOrganizer(conferenceUUID, randomUser, someOrganizer);
    }

    /**
     * You can't demote someone who wasn't an organizer to begin with...
     */
    @Test(timeout = 50, expected = NullUserException.class)
    public void testRemoveInvalidOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.removeOrganizer(conferenceUUID, myUser, randomUser);
    }

    @Test(timeout = 50)
    public void testGetSpeakers() {

    }

    @Test(timeout = 50)
    public void testGetSpeakersInsufficientPermission() {

    }

    @Test(timeout = 50)
    public void testGetOrganizers() {

    }

    @Test(timeout = 50)
    public void testGetOrganizersInsufficientPermission() {

    }

    @Test(timeout = 50)
    public void testGetAttendees() {

    }

    @Test(timeout = 50)
    public void testGetAttendeesInsufficientPermission() {

    }

    @Test(timeout = 50)
    public void testCreateEvent() {

    }

    @Test(timeout = 50)
    public void testCreateEventInvalidName() {

    }

    @Test(timeout = 50)
    public void testCreateEventInsufficientPermission() {

    }

    /**
     * TODO: Room test cases
     */


    /* Test attendee operations */

    @Test(timeout = 50)
    public void testJoinConference() {

    }

    @Test(timeout = 50)
    public void testLeaveConference() {

    }

    @Test(timeout = 50, expected = NullUserException.class)
    public void testLeaveConferenceInvalidUser() {

    }

    @Test(timeout = 50, expected = LoneOrganizerException.class)
    public void testLeaveConferenceLastOrganizer() {

    }

    @Test(timeout = 50)
    public void testRegisterForEvent() {

    }

    @Test(timeout = 50, expected = NullEventException.class)
    public void testRegisterForEventInvalidEvent() {

    }

    @Test(timeout = 50, expected = PermissionException.class)
    public void testRegisterForEventInsufficientPermission() {

    }

    @Test(timeout = 50)
    public void testUnRegisterForEvent() {

    }

    @Test(timeout = 50, expected = NullEventException.class)
    public void testUnregisterForInvalidEvent() {

    }

    @Test(timeout = 50, expected = NullEventException.class)
    public void testUnregisterForUnregisteredEvent() {

    }

    @Test(timeout = 50, expected = PermissionException.class)
    public void testUnregisterForEventInsufficientPermission() {

    }

    @Test(timeout = 50)
    public void testGetEvents() {

    }

    @Test(timeout = 50, expected = PermissionException.class)
    public void testGetEventsInsufficientPermission() {

    }

    /* Test speaker operations */

    @Test(timeout = 50)
    public void testListAttendees() {

    }

    @Test(timeout = 50, expected = NullEventException.class)
    public void testListAttendeesInvalidEvent() {

    }

    @Test(timeout = 50, expected = PermissionException.class)
    public void testListAttendeesInsufficientPermission() {

    }

    @Test(timeout = 50)
    public void testCreateEventConversation() {

    }

    @Test(timeout = 50, expected = NullEventException.class)
    public void testListEventConversationInvalidEvent() {

    }

    @Test(timeout = 50, expected = PermissionException.class)
    public void testListEventConversationInsufficientPermission() {

    }
}
