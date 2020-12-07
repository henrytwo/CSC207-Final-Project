package convention;

import convention.exception.InvalidSortMethodException;
import convention.schedule.ScheduleConstants;
import gateway.DocumentPrinter;
import gateway.IDocumentPrinter;
import util.Pair;
import util.TableTools;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class ScheduleController {
    String title;
    ConferenceController conferenceController;

    public ScheduleController(ConferenceController conferenceController) {
        this.conferenceController = conferenceController;
    }

    public String compileSchedule(List<Pair<UUID, UUID>> listOfPairs) {
        List<List<String>> table = new ArrayList<>();
        for (Pair<UUID, UUID> pair : listOfPairs) {
            List<String> speakerNames = new ArrayList<>();
            for (UUID speakerUUID : conferenceController.getConferenceManager().getEventManager(pair.getValue()).getEvent(pair.getKey()).getSpeakers()) {
                speakerNames.add(conferenceController.getUserManager().getUserUsername(speakerUUID));
            }
            String speakers = String.join(",", speakerNames);
            List<String> eventInfoStrings = new ArrayList<>(
                    Arrays.asList(
                            conferenceController.getConferenceManager().getConferenceName(pair.getValue()),
                            conferenceController.getConferenceManager().getEventManager(pair.getValue()).getEventTitle(pair.getKey()),
                            speakers,
                            conferenceController.getConferenceManager().getRoomManager(pair.getValue()).getRoomLocation(conferenceController.getConferenceManager().getEventManager(pair.getValue()).getEvent(pair.getKey()).getRoomUUID())
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
        Set<UUID> conferenceUUIDSet;

        switch (sortByMethod) {
            case REGISTERED:
                UUID userUUID = (UUID) arguments.get("registered");
                this.title = "Schedule of events "
                        + conferenceController.getUserManager().getUserUsername(userUUID)
                        + "signed up for";
                conferenceUUIDSet = conferenceController.getUserConferences(userUUID);
                for (UUID conferenceUUID: conferenceUUIDSet) {
                    Set<UUID> eventsRegisteredInConference = conferenceController.getEventController().getAttendeeEvents(conferenceUUID, userUUID);
                    for (UUID eventUUID : eventsRegisteredInConference) {
                        Pair<UUID, UUID> eventConferenceUUIDPair = new Pair<>(eventUUID, conferenceUUID);
                        listOfPairs.add(eventConferenceUUIDPair);
                    }
                }
                break;
            case SPEAKER:
                UUID speakerUUID = (UUID) arguments.get("speaker");
                this.title = "Schedule of events with speaker "
                        + conferenceController.getUserManager().getUserUsername(speakerUUID);
                conferenceUUIDSet = conferenceController.getUserConferences(speakerUUID);
                for (UUID conferenceUUID : conferenceUUIDSet) {
                    Set<UUID> speakerEventsInConference = conferenceController.getEventController().getSpeakerEvents(conferenceUUID, speakerUUID);
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
                conferenceUUIDSet = conferenceController.getDayConferences(date);
                for (UUID conferenceUUID : conferenceUUIDSet) {
                    Set<UUID> dayEventsInConference = conferenceController.getEventController().getDayEvents(conferenceUUID, date);
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

