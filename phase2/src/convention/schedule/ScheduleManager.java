package convention.schedule;

import convention.conference.ConferenceManager;
import user.UserManager;
import util.Pair;
import util.TableTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ScheduleManager {
    String title;
    UserManager userManager;
    ConferenceManager conferenceManager;

    public ScheduleManager(UserManager userManager, ConferenceManager conferenceManager) {
        this.userManager = userManager;
        this.conferenceManager = conferenceManager;
    }


    public String compileSchedule(ScheduleConstants.sortByMethods sortByMethod, List<Pair<UUID, UUID>> ListOfPairs) {
//        TODO: figure out how to add title for diff sortBy
        switch (sortByMethod) {
            case REGISTERED:
                break;
            case SPEAKER:
                break;
            case DATE:
                break;

        }
        List<List<String>> table = new ArrayList<>();
        for (Pair<UUID, UUID> pair : ListOfPairs) {
            ArrayList<String> speakerNames = new ArrayList<>();
            for (UUID speakerUUID : conferenceManager.getEventManager(pair.getValue()).getEvent(pair.getKey()).getSpeakers()) {
                speakerNames.add(userManager.getUserUsername(speakerUUID));
            }
            String speakers = String.join(",", speakerNames);
            ArrayList<String> eventInfoStrings = new ArrayList<>(
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
}
