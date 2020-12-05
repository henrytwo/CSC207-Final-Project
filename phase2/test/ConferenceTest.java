import contact.ContactManager;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.calendar.TimeRange;
import convention.conference.ConferenceManager;
import convention.exception.*;
import messaging.ConversationController;
import messaging.ConversationManager;
import org.junit.Before;
import org.junit.Test;
import user.UserManager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class ConferenceTest {

    /* Setup tests */
    UUID myUser = UUID.randomUUID();
    UUID randomUser = UUID.randomUUID();
    UUID someOrganizer = UUID.randomUUID();
    UUID someAttendee = UUID.randomUUID();
    UUID someAttendeeB = UUID.randomUUID();
    UUID someAttendeeC = UUID.randomUUID();
    UUID someSpeaker = UUID.randomUUID();
    UUID someSpeakerB = UUID.randomUUID();
    UUID someSpeakerC = UUID.randomUUID();
    UUID randomConference = UUID.randomUUID();
    UUID randomEvent = UUID.randomUUID();

    String emptyString = "";
    String conferenceNameA = "Conference A";
    String conferenceNameB = "Conference B";
    String eventNameA = "Event A";
    String eventNameB = "Event B";
    String roomA = "Room A";

    LocalDateTime dateG = LocalDateTime.of(2015,
            Month.JULY, 29, 19, 30, 40);
    LocalDateTime dateH = LocalDateTime.of(2058,
            Month.JULY, 29, 19, 30, 40);

    TimeRange timeRangeD = new TimeRange(dateG, dateH);

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

    LocalDateTime dateE = LocalDateTime.of(2030,
            Month.APRIL, 2, 1, 3, 20);
    LocalDateTime dateF = LocalDateTime.of(2049,
            Month.AUGUST, 29, 19, 30, 40);

    TimeRange timeRangeC = new TimeRange(dateE, dateF);
    RoomController roomController;
    EventController eventController;
    ConferenceController conferenceController;
    ConversationController conversationController;


    @Before
    public void init() {
        UserManager userManager = new UserManager();
        ConversationManager conversationManager = new ConversationManager();
        ConferenceManager conferenceManager = new ConferenceManager();
        ContactManager contactManager = new ContactManager();

        // Convention controllers
        conversationController = new ConversationController(contactManager, conversationManager, userManager);
        roomController = new RoomController(conferenceManager, userManager);
        eventController = new EventController(conferenceManager, conversationManager, userManager);
        conferenceController = new ConferenceController(conversationManager, eventController, conferenceManager, userManager);
    }

    // TestView with and without permission

    // Admin tasks

    // Create conf X
    // Edit conf
    // Add organizer X
    // Remove organizer X
    // Remove attendee
    // Edit date X
    // Edit name X
    // Delete conf X

    // Create events
    // Assign a speaker
    // Edit events
    // Delete events
    // Revoke speaker access if they don't have any more events

    // Create room
    // Edit room
    // Delete room

    // User tasks

    // Join convention
    //    - Invite? Search?
    // Register in events
    // List events
    //    - TestView the different ways to sort

    // Speaker tasks

    // List events they're running
    // Create convo for an events

    /* Conference creation */

    /**
     * The convention should exist after it is created... duh
     */
    @Test(timeout = 500)
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
    @Test(timeout = 500, expected = InvalidNameException.class)
    public void testCreateConferenceInvalidName() {
        conferenceController.createConference(emptyString, timeRangeA, myUser);
    }

    /* Editing a convention */
    @Test(timeout = 500)
    public void testEditConferenceName() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceName(conferenceUUID, myUser, conferenceNameB);
        assertEquals(conferenceController.getConferenceName(conferenceUUID), conferenceNameB);
    }

    /**
     * You need a name...
     */
    @Test(timeout = 500, expected = InvalidNameException.class)
    public void testEditConferenceNameInvalidName() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceName(conferenceUUID, myUser, emptyString);
    }

    /**
     * You must be an organizer
     */
    @Test(timeout = 500, expected = PermissionException.class)
    public void testEditConferenceNameInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceName(conferenceUUID, randomUser, conferenceNameB);
    }

    @Test(timeout = 500)
    public void testEditConferenceDates() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceTimeRange(conferenceUUID, myUser, timeRangeB);

        assertEquals(conferenceController.getConferenceTimeRange(conferenceUUID), timeRangeB);
    }

    /**
     * You must be an organizer
     */
    @Test(timeout = 500, expected = PermissionException.class)
    public void testEditConferenceDatesInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.setConferenceTimeRange(conferenceUUID, randomUser, timeRangeB);
    }

    /* Deleting a convention */
    @Test(timeout = 500)
    public void testDeleteConference() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        conferenceController.deleteConference(conferenceUUID, myUser);
        assertFalse(conferenceController.conferenceExists(conferenceUUID));
    }

    /**
     * You can't delete a convention that doesn't exist
     */
    @Test(timeout = 500, expected = NullConferenceException.class)
    public void testDeleteInvalidConference() {
        conferenceController.deleteConference(randomConference, myUser);
    }

    /**
     * Only organizers can do this
     */
    @Test(timeout = 500, expected = PermissionException.class)
    public void testDeleteConferenceInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        conferenceController.deleteConference(conferenceUUID, randomUser);
    }

    /* Time range */
    @Test(timeout = 500)
    public void testTimeRange() {
        new TimeRange(dateA, dateB);
    }

    @Test(timeout = 500, expected = InvalidTimeRangeException.class)
    public void testInvalidTimeRange() {
        new TimeRange(dateB, dateA);
    }

    /* Managing organizers */

    @Test(timeout = 500)
    public void testAddOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        // TestView that there is only the initial user
        Set<UUID> organizers = conferenceController.getOrganizers(conferenceUUID, myUser);
        assertTrue(organizers.size() == 1 && organizers.contains(myUser));

        // Add the new organizer
        conferenceController.addOrganizer(conferenceUUID, myUser, someOrganizer);

        // Update pointer
        organizers = conferenceController.getOrganizers(conferenceUUID, myUser);

        // Ensure new organizer has been added successfully
        assertTrue(organizers.size() == 2 && organizers.contains(myUser) && organizers.contains(someOrganizer));
    }

    /**
     * Only organizers can do this
     */
    @Test(timeout = 500, expected = PermissionException.class)
    public void testAddOrganizerInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.addOrganizer(conferenceUUID, randomUser, someOrganizer);
    }

    @Test(timeout = 500)
    public void testRemoveOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        // Add the new organizer
        conferenceController.addOrganizer(conferenceUUID, myUser, someOrganizer);

        // TestView that both users have been added
        Set<UUID> organizers = conferenceController.getOrganizers(conferenceUUID, myUser);
        assertTrue(organizers.size() == 2 && organizers.contains(myUser) && organizers.contains(someOrganizer));

        // Remove the organizer
        conferenceController.removeOrganizer(conferenceUUID, myUser, someOrganizer);

        // Update pointer
        organizers = conferenceController.getOrganizers(conferenceUUID, myUser);

        // TestView that the organizer has indeed been removed
        assertEquals(organizers.size(), 1);
        assertTrue(organizers.contains(myUser));
        assertFalse(organizers.contains(someOrganizer));
    }

    /**
     * You can't remove yourself if you're the last organizer
     */
    @Test(timeout = 500, expected = LoneOrganizerException.class)
    public void testRemoveLastOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        // Remove the organizer
        conferenceController.removeOrganizer(conferenceUUID, myUser, myUser);
    }

    /**
     * Only organizers can do this
     */
    @Test(timeout = 500, expected = PermissionException.class)
    public void testRemoveOrganizerInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.removeOrganizer(conferenceUUID, randomUser, someOrganizer);
    }

    /**
     * You can't demote someone who wasn't an organizer to begin with...
     */
    @Test(timeout = 500, expected = NullUserException.class)
    public void testRemoveInvalidOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.removeOrganizer(conferenceUUID, myUser, randomUser);
    }

    @Test(timeout = 500)
    public void testGetSpeakers() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        assertTrue(eventController.getEventSpeakers(conferenceUUID, myUser, eventUUID).contains(someSpeaker));
        assertEquals(eventController.getEventSpeakers(conferenceUUID, myUser, eventUUID).size(), 1);
    }

    @Test(timeout = 500)
    public void testIsSpeaker() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        assertFalse(conferenceController.isSpeaker(conferenceUUID, myUser, someSpeaker));

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        assertTrue(conferenceController.isSpeaker(conferenceUUID, myUser, someSpeaker));
        assertFalse(conferenceController.isSpeaker(conferenceUUID, myUser, randomUser));
    }


    @Test(timeout = 500)
    public void testGetOrganizers() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        assertTrue(conferenceController.getOrganizers(conferenceUUID, myUser).contains(myUser));
        assertEquals(conferenceController.getOrganizers(conferenceUUID, myUser).size(), 1);
    }

    @Test(timeout = 500)
    public void testIsOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        assertFalse(conferenceController.isOrganizer(conferenceUUID, myUser, someOrganizer));
        conferenceController.addOrganizer(conferenceUUID, myUser, someOrganizer);

        assertTrue(conferenceController.isOrganizer(conferenceUUID, myUser, someOrganizer));
        assertFalse(conferenceController.isOrganizer(conferenceUUID, myUser, randomUser));
    }

    @Test(timeout = 500)
    public void testGetAttendees() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        assertEquals(conferenceController.getAttendees(conferenceUUID, myUser).size(), 0);

        conferenceController.addAttendee(conferenceUUID, randomUser);

        assertTrue(conferenceController.getAttendees(conferenceUUID, myUser).contains(randomUser));
        assertEquals(conferenceController.getAttendees(conferenceUUID, myUser).size(), 1);
    }

    @Test(timeout = 500)
    public void testIsAttendee() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        assertFalse(conferenceController.isAttendee(conferenceUUID, myUser, someAttendee));
        conferenceController.addAttendee(conferenceUUID, someAttendee);

        assertTrue(conferenceController.isAttendee(conferenceUUID, myUser, someAttendee));
        assertFalse(conferenceController.isAttendee(conferenceUUID, myUser, randomUser));
    }

    @Test(timeout = 500)
    public void testCreateEvent() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        assertEquals(eventController.getEvents(conferenceUUID, myUser).size(), 0);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        assertEquals(eventController.getEvents(conferenceUUID, myUser).size(), 1);
        assertTrue(eventController.getEvents(conferenceUUID, myUser).contains(eventUUID));
    }

    @Test(timeout = 500, expected = InvalidNameException.class)
    public void testCreateEventInvalidName() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, emptyString, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });
    }

    @Test(timeout = 500, expected = PermissionException.class)
    public void testCreateEventInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, randomUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });
    }

    /**
     * TODO: Room test cases
     */


    /* TestView attendee operations */
    @Test(timeout = 500)
    public void testJoinConference() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        conferenceController.addAttendee(conferenceUUID, randomUser);
        assertEquals(eventController.getEventAttendees(conferenceUUID, myUser, eventUUID).size(), 0);

        eventController.registerForEvent(conferenceUUID, randomUser, randomUser, eventUUID);

        assertEquals(eventController.getEventAttendees(conferenceUUID, myUser, eventUUID).size(), 1);
        assertTrue(eventController.getEventAttendees(conferenceUUID, myUser, eventUUID).contains(randomUser));
    }

    @Test(timeout = 500, expected = FullEventException.class)
    public void testJoinConferenceFullRoom() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 1);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        conferenceController.addAttendee(conferenceUUID, randomUser);
        conferenceController.addAttendee(conferenceUUID, someAttendee);

        assertEquals(eventController.getEventAttendees(conferenceUUID, myUser, eventUUID).size(), 0);

        eventController.registerForEvent(conferenceUUID, randomUser, randomUser, eventUUID);

        assertEquals(eventController.getEventAttendees(conferenceUUID, myUser, eventUUID).size(), 1);
        assertTrue(eventController.getEventAttendees(conferenceUUID, myUser, eventUUID).contains(randomUser));

        eventController.registerForEvent(conferenceUUID, someAttendee, someAttendee, eventUUID);
    }

    @Test(timeout = 500, expected = PermissionException.class)
    public void testJoinConferenceRandomUser() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        eventController.registerForEvent(conferenceUUID, randomUser, randomUser, eventUUID);
    }

    @Test(timeout = 500)
    public void testLeaveConferenceAttendee() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.addAttendee(conferenceUUID, someAttendee);

        assertTrue(conferenceController.getAttendees(conferenceUUID, myUser).contains(someAttendee));

        conferenceController.leaveConference(conferenceUUID, someAttendee, someAttendee);

        assertFalse(conferenceController.getAttendees(conferenceUUID, myUser).contains(someAttendee));
    }

    @Test(timeout = 500)
    public void testLeaveConferenceSpeaker() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        assertTrue(conferenceController.getSpeakers(conferenceUUID, myUser).contains(someSpeaker));
        assertTrue(eventController.getEventSpeakers(conferenceUUID, myUser, eventUUID).contains(someSpeaker));

        conferenceController.leaveConference(conferenceUUID, someSpeaker, someSpeaker);

        assertFalse(conferenceController.getSpeakers(conferenceUUID, myUser).contains(someSpeaker));
        assertFalse(eventController.getEventSpeakers(conferenceUUID, myUser, eventUUID).contains(someSpeaker));
    }

    @Test(timeout = 500, expected = NullConferenceException.class)
    public void testLeaveConferenceInvalidConference() {
        conferenceController.leaveConference(randomConference, randomUser, randomUser);
    }

    @Test(timeout = 500, expected = PermissionException.class)
    public void testLeaveConferenceInvalidUser() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        conferenceController.leaveConference(conferenceUUID, randomUser, randomUser);
    }

    @Test(timeout = 500, expected = LoneOrganizerException.class)
    public void testLeaveConferenceLastOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        conferenceController.leaveConference(conferenceUUID, myUser, myUser);
    }

    @Test(timeout = 500)
    public void testRegisterForEvent() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        assertTrue(eventController.getAttendeeEvents(conferenceUUID, myUser).size() == 0);

        eventController.registerForEvent(conferenceUUID, myUser, myUser, eventUUID);

        assertTrue(eventController.getAttendeeEvents(conferenceUUID, myUser).contains(eventUUID));
        assertEquals(eventController.getAttendeeEvents(conferenceUUID, myUser).size(), 1);
    }

    @Test(timeout = 500, expected = NullEventException.class)
    public void testRegisterForEventInvalidEvent() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        eventController.registerForEvent(conferenceUUID, myUser, myUser, randomEvent);
    }

    @Test(timeout = 500, expected = PermissionException.class)
    public void testRegisterForEventInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        eventController.registerForEvent(conferenceUUID, randomUser, randomUser, eventUUID);
    }

    @Test(timeout = 500)
    public void testUnRegisterForEvent() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        eventController.registerForEvent(conferenceUUID, myUser, myUser, eventUUID);

        assertTrue(eventController.getAttendeeEvents(conferenceUUID, myUser).contains(eventUUID));
        assertEquals(eventController.getAttendeeEvents(conferenceUUID, myUser).size(), 1);

        eventController.unregisterForEvent(conferenceUUID, myUser, myUser, eventUUID);

        assertFalse(eventController.getAttendeeEvents(conferenceUUID, myUser).contains(eventUUID));
        assertEquals(eventController.getAttendeeEvents(conferenceUUID, myUser).size(), 0);
    }

    @Test(timeout = 500, expected = PermissionException.class)
    public void testUnregisterForEventInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        eventController.registerForEvent(conferenceUUID, myUser, myUser, eventUUID);
        eventController.registerForEvent(conferenceUUID, someAttendee, someAttendee, eventUUID);

        eventController.unregisterForEvent(conferenceUUID, someAttendee, myUser, eventUUID);
    }

    @Test(timeout = 500, expected = PermissionException.class)
    public void testUnregisterAttendeeForEventAsOrganizer() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        eventController.registerForEvent(conferenceUUID, myUser, myUser, eventUUID);
        eventController.registerForEvent(conferenceUUID, someAttendee, someAttendee, eventUUID);

        assertEquals(eventController.getEventAttendees(conferenceUUID, myUser, eventUUID).size(), 2);

        eventController.unregisterForEvent(conferenceUUID, myUser, someAttendee, eventUUID);

        assertFalse(eventController.getEventAttendees(conferenceUUID, myUser, eventUUID).contains(someSpeaker));
        assertEquals(eventController.getEventAttendees(conferenceUUID, myUser, eventUUID).size(), 1);
    }

    @Test(timeout = 500)
    public void testGetEvents() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        assertEquals(eventController.getEvents(conferenceUUID, myUser).size(), 0);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        assertEquals(eventController.getEvents(conferenceUUID, myUser).size(), 1);

        UUID room2UUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);
        UUID event2UUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, room2UUID, new HashSet<UUID>() {
            {
                add(someSpeakerB);
            }
        });

        assertEquals(eventController.getEvents(conferenceUUID, myUser).size(), 2);
    }

    @Test(timeout = 500)
    public void testGetAttendeeEvents() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.addAttendee(conferenceUUID, someAttendee);

        assertEquals(eventController.getAttendeeEvents(conferenceUUID, myUser).size(), 0);

        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        eventController.registerForEvent(conferenceUUID, someAttendee, someAttendee, eventUUID);

        assertEquals(eventController.getAttendeeEvents(conferenceUUID, someAttendee).size(), 1);

        UUID room2UUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);
        UUID event2UUID = eventController.createEvent(conferenceUUID, myUser, eventNameB, timeRangeA, room2UUID, new HashSet<UUID>() {
            {
                add(someSpeakerB);
            }
        });

        eventController.registerForEvent(conferenceUUID, someAttendee, someAttendee, event2UUID);

        assertEquals(eventController.getAttendeeEvents(conferenceUUID, someAttendee).size(), 2);

        UUID room3UUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);
        UUID event3UUID = eventController.createEvent(conferenceUUID, myUser, eventNameB, timeRangeA, room3UUID, new HashSet<UUID>() {
            {
                add(someSpeakerC);
            }
        });

        assertEquals(eventController.getAttendeeEvents(conferenceUUID, someAttendee).size(), 2);
    }

    @Test(timeout = 500, expected = CalendarDoubleBookingException.class)
    public void testDoubleBookRoom() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.addAttendee(conferenceUUID, someAttendee);

        assertEquals(eventController.getAttendeeEvents(conferenceUUID, myUser).size(), 0);

        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);
        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        UUID event2UUID = eventController.createEvent(conferenceUUID, myUser, eventNameB, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeakerB);
            }
        });
    }

    @Test(timeout = 500, expected = SpeakerDoubleBookingException.class)
    public void testDoubleBookSpeaker() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.addAttendee(conferenceUUID, someAttendee);

        assertEquals(eventController.getAttendeeEvents(conferenceUUID, myUser).size(), 0);

        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);
        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        UUID room2UUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);
        UUID event2UUID = eventController.createEvent(conferenceUUID, myUser, eventNameB, timeRangeA, room2UUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });
    }

    @Test(timeout = 500)
    public void testGetSpeakerEvents() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeD, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        assertEquals(eventController.getSpeakerEvents(conferenceUUID, myUser).size(), 0);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        assertEquals(eventController.getSpeakerEvents(conferenceUUID, someSpeaker).size(), 1);

        UUID room2UUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);
        UUID event2UUID = eventController.createEvent(conferenceUUID, myUser, eventNameB, timeRangeB, room2UUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        assertEquals(eventController.getSpeakerEvents(conferenceUUID, someSpeaker).size(), 2);

        UUID room3UUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);
        UUID event3UUID = eventController.createEvent(conferenceUUID, myUser, eventNameB, timeRangeC, room3UUID, new HashSet<UUID>() {
            {
                add(someSpeakerB);
            }
        });

        assertEquals(eventController.getSpeakerEvents(conferenceUUID, someSpeaker).size(), 2);
    }

    // TestView conflicts

    @Test(timeout = 500, expected = PermissionException.class)
    public void testGetEventsInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        eventController.getEvents(conferenceUUID, randomUser);
    }

    /* TestView speaker operations */

    @Test(timeout = 500)
    public void testListAttendees() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);

        conferenceController.addAttendee(conferenceUUID, someAttendee);
        conferenceController.addAttendee(conferenceUUID, someAttendeeB);

        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        assertEquals(eventController.getEventAttendees(conferenceUUID, someSpeaker, eventUUID).size(), 0);

        eventController.registerForEvent(conferenceUUID, someAttendee, someAttendee, eventUUID);
        assertEquals(eventController.getEventAttendees(conferenceUUID, someSpeaker, eventUUID).size(), 1);
        assertTrue(eventController.getEventAttendees(conferenceUUID, someSpeaker, eventUUID).contains(someAttendee));

        eventController.registerForEvent(conferenceUUID, someAttendeeB, someAttendeeB, eventUUID);
        assertEquals(eventController.getEventAttendees(conferenceUUID, someSpeaker, eventUUID).size(), 2);
        assertTrue(eventController.getEventAttendees(conferenceUUID, someSpeaker, eventUUID).contains(someAttendee));
        assertTrue(eventController.getEventAttendees(conferenceUUID, someSpeaker, eventUUID).contains(someAttendeeB));
    }

    @Test(timeout = 500, expected = NullEventException.class)
    public void testListAttendeesInvalidEvent() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        eventController.getEventAttendees(conferenceUUID, myUser, randomEvent);
    }

    @Test(timeout = 500, expected = PermissionException.class)
    public void testListAttendeesInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        conferenceController.addAttendee(conferenceUUID, someAttendee);

        eventController.getEventAttendees(conferenceUUID, someAttendee, randomEvent);
    }

    @Test(timeout = 500)
    public void testCreateEventConversation() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        conferenceController.addAttendee(conferenceUUID, someAttendee);
        conferenceController.addAttendee(conferenceUUID, someAttendeeB);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        eventController.registerForEvent(conferenceUUID, someAttendee, someAttendee, eventUUID);

        assertEquals(conversationController.getConversationList(someAttendee).size(), 0);

        UUID eventConversationUUID = eventController.createEventConversation(conferenceUUID, someSpeaker, eventUUID);
        assertEquals(conversationController.getConversationList(someAttendee).size(), 1);

        eventController.registerForEvent(conferenceUUID, someAttendeeB, someAttendeeB, eventUUID);
        assertEquals(conversationController.getConversationList(someAttendee).size(), 1);

        System.out.println(conversationController.getMessages(someAttendee, eventConversationUUID));

        assertEquals(conversationController.getMessages(someAttendee, eventConversationUUID).size(), 1);
    }

    @Test(timeout = 500, expected = PermissionException.class)
    public void testListEventConversationInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference(conferenceNameA, timeRangeA, myUser);
        UUID roomUUID = roomController.createRoom(conferenceUUID, myUser, roomA, 2);

        conferenceController.addAttendee(conferenceUUID, someAttendee);
        conferenceController.addAttendee(conferenceUUID, someAttendeeB);

        UUID eventUUID = eventController.createEvent(conferenceUUID, myUser, eventNameA, timeRangeA, roomUUID, new HashSet<UUID>() {
            {
                add(someSpeaker);
            }
        });

        UUID eventConversationUUID = eventController.createEventConversation(conferenceUUID, randomUser, eventUUID);
    }
}
