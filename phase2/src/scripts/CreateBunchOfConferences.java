package scripts;

import contact.ContactManager;
import convention.EventController;
import convention.calendar.TimeRange;
import convention.conference.ConferenceManager;
import convention.event.EventManager;
import convention.room.RoomManager;
import gateway.Serializer;
import messaging.ConversationManager;
import user.UserManager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This is a quick script to create a lot of conferences for testing purposes
 * <p>
 * This is some really ugly testing code plz don't mark
 * <p>
 * Seriously -- please do not mark this file
 */
public class CreateBunchOfConferences {
    public static void main(String[] args) {
        Serializer<ConversationManager> conversationManagerSerializer = new Serializer<>("conversationManager.ser");
        Serializer<UserManager> userManagerSerializer = new Serializer<>("userManager.ser");
        Serializer<ContactManager> contactManagerSerializer = new Serializer<>("contactManager.ser");
        Serializer<ConferenceManager> conferenceManagerSerializer = new Serializer<>("conferenceManager.ser");

        UserManager userManager = userManagerSerializer.load(new UserManager());
        ContactManager contactManager = contactManagerSerializer.load(new ContactManager());
        ConversationManager conversationManager = conversationManagerSerializer.load(new ConversationManager());
        ConferenceManager conferenceManager = conferenceManagerSerializer.load(new ConferenceManager());

        EventController eventController = new EventController(conferenceManager, conversationManager, userManager);

        Set<UUID> attendeeUserUUIDs = new HashSet<>();

        // Create test users
        for (int i = 0; i < 3; i++) {
            userManager.registerUser("User " + i, "Userson", "user" + i, "password", false, false);

            // Login in case the user already exists
            UUID newUserUUID = userManager.login("user" + i, "password");

            // Create test conferences
            for (int j = 0; j < 3; j++) {

                LocalDateTime dateA = LocalDateTime.of(2015,
                        Month.JULY, 29, 0, 30);
                LocalDateTime dateB = LocalDateTime.of(2420,
                        Month.JULY, 29, 19, 30);

                TimeRange timeRange = new TimeRange(dateA, dateB);

                UUID newConferenceUUID = conferenceManager.createConference("User " + i + "'s " + j + "th conference", timeRange, newUserUUID);

                RoomManager roomManager = conferenceManager.getRoomManager(newConferenceUUID);
                EventManager eventManager = conferenceManager.getEventManager(newConferenceUUID);

                for (UUID uuid : attendeeUserUUIDs) {
                    conferenceManager.addAttendee(newConferenceUUID, uuid);
                }

                // create test rooms
                for (int p = 0; p < 3; p++) {

                    Set<UUID> speakerUserUUIDs = new HashSet<UUID>();

                    for (int z = 0; z < 3; z++) {
                        UUID newUUID = userManager.registerUser("Speaker " + i + "!" + p + "!" + z, "Speaker", "speaker" + i + "!" + p + "!" + z, "password", false, false);

                        if (newUUID != null) {
                            speakerUserUUIDs.add(newUUID);
                        }
                    }

                    UUID newRoomUUID = roomManager.createRoom("BA123" + p, 69);

                    // create test events
                    for (int q = 0; q < 3; q++) {
                        LocalDateTime eventStart = LocalDateTime.of(2015 + q, Month.JULY, 29, 0, 30);
                        LocalDateTime eventEnd = LocalDateTime.of(2015 + q, Month.AUGUST, 30, 1, 30);

                        TimeRange eventTimeRange = new TimeRange(eventStart, eventEnd);

                        UUID newEventUUID = eventController.createEvent(newConferenceUUID, newUserUUID, "Test event " + q, eventTimeRange, newRoomUUID, new HashSet<>(speakerUserUUIDs));

                    }
                }
            }

            if (i % 2 == 0) {
                attendeeUserUUIDs.add(newUserUUID);
            }
        }

        userManager.clearCurrentUser();

        userManagerSerializer.save(userManager);
        contactManagerSerializer.save(contactManager);
        conversationManagerSerializer.save(conversationManager);
        conferenceManagerSerializer.save(conferenceManager);
    }
}
