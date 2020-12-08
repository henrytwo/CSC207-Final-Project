package convention;

import convention.conference.ConferenceManager;
import convention.event.EventManager;
import convention.exception.InvalidSortMethodException;
import convention.room.RoomManager;
import convention.schedule.ScheduleConstants;
import gateway.IDocumentPrinter;
import user.UserManager;
import util.Pair;
import util.TableTools;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

/**
 * Generates schedules of events for the user
 */
public class ScheduleController {
    private String title;
    private IDocumentPrinter documentPrinter;
    private UserManager userManager;
    private ConferenceManager conferenceManager;
    private EventController eventController;

    /**
     * Constructs ScheduleController
     *
     * @param documentPrinter   printer used to display final schedule
     * @param userManager       instance of user manager
     * @param conferenceManager instance of conference manager
     * @param eventController   instance of event controller
     */
    public ScheduleController(IDocumentPrinter documentPrinter, UserManager userManager, ConferenceManager conferenceManager, EventController eventController) {
        this.userManager = userManager;
        this.conferenceManager = conferenceManager;
        this.documentPrinter = documentPrinter;
        this.eventController = eventController;
    }

    /**
     * Compiles conference-event pairs into a table, which is stringified
     *
     * @param listOfPairs list of event-conference pairs
     * @return string of compiled table
     */
    private String compileSchedule(List<Pair<UUID, UUID>> listOfPairs) {
        List<List<String>> table = new ArrayList<>();

        table.add(Arrays.asList("Conference Name", "Event Title", "Speakers", "Location"));

        for (Pair<UUID, UUID> pair : listOfPairs) {
            List<String> speakerNames = new ArrayList<>();

            UUID conferenceUUID = pair.getValue();
            UUID eventUUID = pair.getKey();

            EventManager eventManager = conferenceManager.getEventManager(conferenceUUID);
            RoomManager roomManager = conferenceManager.getRoomManager(conferenceUUID);

            // Compile list of speakers
            for (UUID speakerUUID : eventManager.getEvent(eventUUID).getSpeakers()) {
                speakerNames.add(userManager.getUserFullName(speakerUUID));
            }

            String speakers = String.join(", ", speakerNames);

            // Add row to the list
            List<String> eventInfoStrings = new ArrayList<>(
                    Arrays.asList(
                            conferenceManager.getConferenceName(conferenceUUID),
                            eventManager.getEventTitle(eventUUID),
                            speakers,
                            roomManager.getRoomLocation(eventManager.getEvent(eventUUID).getRoomUUID())
                    )
            );

            table.add(eventInfoStrings);
        }

        TableTools tableTools = new TableTools(table);
        return tableTools.stringifyTable(this.title) + (listOfPairs.size() == 0 ? "<br/><br/>NO RECORDS FOUND" : "");
    }

    /**
     * Generates an ordered list of Conference-Event pairs of events that the user is registered in
     *
     * @param userUUID UUID of user to operate on
     * @return
     */
    private List<Pair<UUID, UUID>> getRegisteredEventPairs(UUID userUUID) {
        List<Pair<UUID, UUID>> listOfPairs = new ArrayList<>();
        Set<UUID> conferenceUUIDSet = new HashSet<>();

        // Compiles list of relevant conferences
        for (UUID conferenceUUID : conferenceManager.getConferences()) {
            if (conferenceManager.isAttendee(conferenceUUID, userUUID)) {
                conferenceUUIDSet.add(conferenceUUID);
            }
        }

        // Compiles list of relevant events
        for (UUID conferenceUUID : conferenceUUIDSet) {
            Set<UUID> eventsRegisteredInConference = eventController.getAttendeeEvents(conferenceUUID, userUUID);
            for (UUID eventUUID : eventsRegisteredInConference) {
                Pair<UUID, UUID> eventConferenceUUIDPair = new Pair<>(eventUUID, conferenceUUID);
                listOfPairs.add(eventConferenceUUIDPair);
            }
        }

        return listOfPairs;
    }

    /**
     * Generates an ordered list of Conference-Event pairs of events that a speaker is assigned to
     *
     * @param speakerUUID UUID of speaker to operate on
     * @return
     */
    private List<Pair<UUID, UUID>> getSpeakerEventPairs(UUID speakerUUID) {
        List<Pair<UUID, UUID>> listOfPairs = new ArrayList<>();
        Set<UUID> conferenceUUIDSet = new HashSet<>();

        // Compiles list of relevant conferences
        for (UUID conferenceUUID : conferenceManager.getConferences()) {
            if (conferenceManager.isSpeaker(conferenceUUID, speakerUUID)) {
                conferenceUUIDSet.add(conferenceUUID);
            }
        }

        // Compiles list of relevant events
        for (UUID conferenceUUID : conferenceUUIDSet) {
            Set<UUID> speakerEventsInConference = eventController.getSpeakerEvents(conferenceUUID, speakerUUID);
            for (UUID eventUUID : speakerEventsInConference) {
                Pair<UUID, UUID> eventConferenceUUIDPair = new Pair<>(eventUUID, conferenceUUID);
                listOfPairs.add(eventConferenceUUIDPair);
            }
        }

        return listOfPairs;
    }

    /**
     * Generates an ordered list of Conference-Event pairs of events that occur on a particular date
     *
     * @param date Date to print schedule from
     * @return
     */
    private List<Pair<UUID, UUID>> getDateEventPairs(LocalDate date) {
        List<Pair<UUID, UUID>> listOfPairs = new ArrayList<>();
        Set<UUID> conferenceUUIDSet = new HashSet<>();

        // Compiles list of relevant conferences
        for (UUID conferenceUUID : conferenceManager.getConferences()) {
            if (conferenceManager.getTimeRange(conferenceUUID).isInDay(date)) {
                conferenceUUIDSet.add(conferenceUUID);
            }
        }

        // Compiles list of relevant events
        for (UUID conferenceUUID : conferenceUUIDSet) {
            Set<UUID> dayEventsInConference = eventController.getDayEvents(conferenceUUID, date);
            for (UUID eventUUID : dayEventsInConference) {
                Pair<UUID, UUID> eventConferenceUUIDPair = new Pair<>(eventUUID, conferenceUUID);
                listOfPairs.add(eventConferenceUUIDPair);
            }
        }

        return listOfPairs;
    }

    /**
     * Compiles a schedule for the given sort method and data and triggers the system print dialog.
     *
     * @param sortByMethod enum of method to sort by
     * @param arguments    map of arguments
     * @throws IOException
     */
    public void printSchedule(ScheduleConstants.sortByMethods sortByMethod, Map<String, Object> arguments) throws IOException {
        List<Pair<UUID, UUID>> listOfPairs;

        // Compile the list of event-conference pairs
        switch (sortByMethod) {
            case REGISTERED:
                UUID userUUID = (UUID) arguments.get("userUUID");

                listOfPairs = getRegisteredEventPairs(userUUID);
                this.title = String.format("Schedule of events %s signed up for", userManager.getUserFullName(userUUID));
                break;
            case SPEAKER:
                UUID speakerUUID = (UUID) arguments.get("speakerUUID");
                this.title = String.format("Schedule of events with speaker %s", userManager.getUserFullName(speakerUUID));

                listOfPairs = getSpeakerEventPairs(speakerUUID);
                break;
            case DATE:
                LocalDate date = (LocalDate) arguments.get("date");
                this.title = String.format("Schedule of events on %s", arguments.get("date").toString());

                listOfPairs = getDateEventPairs(date);
                break;
            default:
                throw new InvalidSortMethodException();
        }

        // Compile string table
        String scheduleString = compileSchedule(listOfPairs);

        // Trigger print operation
        documentPrinter.print(scheduleString, "schedule");
    }
}

