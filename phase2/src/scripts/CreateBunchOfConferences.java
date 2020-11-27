package scripts;

import contact.ContactManager;
import convention.calendar.TimeRange;
import convention.conference.ConferenceManager;
import gateway.Serializer;
import messaging.ConversationManager;
import user.UserManager;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

/**
 * This is a quick script to create a lot of conferences for testing purposes
 *
 * This is some really ugly testing code plz don't mark
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

        // Create test users
        for (int i = 0; i < 10; i++) {
            userManager.registerUser("User " + i, "Userson", "user"+ i, "password", false, false);

            // Login in case the user already exists
            UUID newUserUUID = userManager.login("user"+ i, "password");

            // Create test conferences
            for (int j = 0; j < 3; j++) {

                LocalDateTime dateA = LocalDateTime.of(2015,
                        Month.JULY, 29, 19, 30, 40);
                LocalDateTime dateB = LocalDateTime.of(2018,
                        Month.JULY, 29, 19, 30, 40);

                TimeRange timeRange = new TimeRange(dateA, dateB);

                UUID newConferenceUUID = conferenceManager.createConference("User " + i + "'s conference", timeRange, newUserUUID);

                // create test rooms or whatever here

            }
        }

        userManager.clearCurrentUser();

        userManagerSerializer.save(userManager);
        contactManagerSerializer.save(contactManager);
        conversationManagerSerializer.save(conversationManager);
        conferenceManagerSerializer.save(conferenceManager);
    }
}
