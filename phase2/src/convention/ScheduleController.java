package convention;

import convention.conference.ConferenceManager;
import convention.exception.InvalidSortMethodException;
import convention.schedule.ScheduleConstants;
import gateway.DocumentPrinter;
import gateway.IDocumentPrinter;
import user.UserManager;
import util.Pair;
import util.TableTools;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ScheduleController {
    String title;
    UserManager userManager;
    ConferenceManager conferenceManager;
    EventController eventController;

    public ScheduleController(UserManager userManager, ConferenceManager conferenceManager, EventController eventController) {
        this.userManager = userManager;
        this.conferenceManager = conferenceManager;
        this.eventController = eventController;
    }


    public String compileSchedule(List<Pair<UUID, UUID>> listOfPairs) {
        List<List<String>> table = new ArrayList<>();
        for (Pair<UUID, UUID> pair : listOfPairs) {
            List<String> speakerNames = new ArrayList<>();
            for (UUID speakerUUID : conferenceManager.getEventManager(pair.getValue()).getEvent(pair.getKey()).getSpeakers()) {
                speakerNames.add(userManager.getUserUsername(speakerUUID));
            }
            String speakers = String.join(",", speakerNames);
            List<String> eventInfoStrings = new ArrayList<>(
                    Arrays.asList(
                            conferenceManager.getConferenceName(pair.getValue()),
                            conferenceManager.getEventManager(pair.getValue()).getEventTitle(pair.getKey()),
                            speakers,
                            conferenceManager.getRoomManager(pair.getValue()).getRoomLocation(conferenceManager.getEventManager(pair.getValue()).getEvent(pair.getKey()).getRoomUUID())
                    )
            );
            table.add(eventInfoStrings);
        }
        TableTools tableTools = new TableTools(table);
        return tableTools.stringifyTable(this.title);
    }

    public void printSchedule(ScheduleConstants.sortByMethods sortByMethod, Map<String, Object> arguments, String fileName) throws IOException {

        // generate list of pairs
        List<Pair<UUID, UUID>> listOfPairs = new ArrayList<>();
        Set<UUID> conferenceUUIDSet = new HashSet<>();

        switch (sortByMethod) {
            case REGISTERED:
                UUID userUUID = (UUID) arguments.get("registered");
                this.title = "Schedule of events "
                        + userManager.getUserUsername(userUUID)
                        + "signed up for";
                for (UUID conferenceUUID : conferenceManager.getConferences()) {
                    if (conferenceManager.isAttendee(conferenceUUID, userUUID)) {
                        conferenceUUIDSet.add(conferenceUUID);
                    }
                }
                for (UUID conferenceUUID: conferenceUUIDSet) {
                    Set<UUID> eventsRegisteredInConference = eventController.getAttendeeEvents(conferenceUUID, userUUID);
                    for (UUID eventUUID : eventsRegisteredInConference) {
                        Pair<UUID, UUID> eventConferenceUUIDPair = new Pair<>(eventUUID, conferenceUUID);
                        listOfPairs.add(eventConferenceUUIDPair);
                    }
                }
                break;
            case SPEAKER:
                UUID speakerUUID = (UUID) arguments.get("speaker");
                this.title = "Schedule of events with speaker "
                        + userManager.getUserUsername(speakerUUID);
                for (UUID conferenceUUID : conferenceManager.getConferences()) {
                    if (conferenceManager.isSpeaker(conferenceUUID, speakerUUID)) {
                        conferenceUUIDSet.add(conferenceUUID);
                    }
                }
                for (UUID conferenceUUID : conferenceUUIDSet) {
                    Set<UUID> speakerEventsInConference = eventController.getSpeakerEvents(conferenceUUID, speakerUUID);
                    for (UUID eventUUID: speakerEventsInConference) {
                        Pair<UUID, UUID> eventConferenceUUIDPair = new Pair<>(eventUUID, conferenceUUID);
                        listOfPairs.add(eventConferenceUUIDPair);
                    }
                }
                break;
            case DATE:
                LocalDate date = (LocalDate) arguments.get("date");
                this.title = "Schedule of events on "
                        + arguments.get("date").toString();
                for (UUID conferenceUUID : conferenceManager.getConferences()) {
                    if (conferenceManager.getTimeRange(conferenceUUID).isInDay(date)) {
                        conferenceUUIDSet.add(conferenceUUID);
                    }
                }
                for (UUID conferenceUUID : conferenceUUIDSet) {
                    Set<UUID> dayEventsInConference = eventController.getDayEvents(conferenceUUID, date);
                    for (UUID eventUUID : dayEventsInConference) {
                        Pair<UUID, UUID> eventConferenceUUIDPair = new Pair<>(eventUUID, conferenceUUID);
                        listOfPairs.add(eventConferenceUUIDPair);
                    }
                }
                break;
            default:
                throw new InvalidSortMethodException();
        }

        String scheduleString = compileSchedule(listOfPairs);
        IDocumentPrinter documentPrinter = new DocumentPrinter();
        documentPrinter.print(scheduleString, fileName);
        }
    }

