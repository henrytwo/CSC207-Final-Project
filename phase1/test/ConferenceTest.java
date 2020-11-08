import conference.ConferenceController;
import org.junit.*;
import util.exception.InvalidTimeRangeException;
import util.exception.NullUserException;
import util.exception.PermissionException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.*;

public class ConferenceTest {

    UUID myUser = UUID.randomUUID();
    UUID randomUser = UUID.randomUUID();
    UUID someOrganizer = UUID.randomUUID();
    UUID someAttendee = UUID.randomUUID();
    UUID someSpeaker = UUID.randomUUID();

    LocalDateTime dateA = LocalDateTime.of(2015,
            Month.JULY, 29, 19, 30, 40);
    LocalDateTime dateB = LocalDateTime.of(2018,
            Month.JULY, 29, 19, 30, 40);

    ConferenceController conferenceController;

    // Test with and without permission

    // Admin tasks

    // Create conf
    // Edit conf
    // Delete conf

    // Create event
    // Edit event
    // Delete event

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

    @Before
    public void init() {
        conferenceController = new ConferenceController();
    }

    @Test(timeout = 50)
    public void testCreateConference() {
        UUID conferenceUUID = conferenceController.createConference("Conference", dateA, dateB, myUser);

        /*
        conferenceController.removeOrganizer(conferenceUUID, UUID.randomUUID(), myUser);


        try {
            conferenceController.deleteConference(conferenceUUID, UUID.randomUUID());
        } catch (PermissionException e) {

        }

        conferenceController.deleteConference(conferenceUUID, myUser);*/
    }

    @Test(timeout = 50, expected = InvalidTimeRangeException.class)
    public void testCreateConferenceInvalidTimeRange() {
        conferenceController.createConference("Conference", dateB, dateA, myUser);
    }

    @Test(timeout = 50)
    public void testAddOrganizer() {
        UUID conferenceUUID = conferenceController.createConference("Conference", dateA, dateB, myUser);

        // Test that there is only the initial user
        Set<UUID> organizers = conferenceController.getOrganizers(conferenceUUID, myUser);
        assertTrue(organizers.size() == 1 && organizers.contains(myUser));

        // Add the new organizer
        conferenceController.addOrganizer(conferenceUUID, myUser, someOrganizer);

        // Ensure new organizer has been added successfully
        assertTrue(organizers.size() == 2 && organizers.contains(myUser) && organizers.contains(someOrganizer));
    }

    @Test(timeout = 50, expected = PermissionException.class)
    public void testAddOrganizerInsufficientPermission() {
        UUID conferenceUUID = conferenceController.createConference("Conference", dateA, dateB, myUser);

        conferenceController.addOrganizer(conferenceUUID, randomUser, someOrganizer);
    }

    @Test(timeout = 50)
    public void testRemoveOrganizer() {
        UUID conferenceUUID = conferenceController.createConference("Conference", dateA, dateB, myUser);

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

    @Test(timeout = 50, expected = NullUserException.class)
    public void testRemoveInvalidOrganizer() {
        UUID conferenceUUID = conferenceController.createConference("Conference", dateA, dateB, myUser);

        conferenceController.removeOrganizer(conferenceUUID, myUser, randomUser);
    }
}
