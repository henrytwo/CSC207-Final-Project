package gui.conference.general;

import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import gui.util.interfaces.IFrame;
import user.UserController;
import util.ControllerBundle;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.util.UUID;

class ConferenceGeneralPresenter {

    IConferenceGeneralView conferenceGeneralView;

    EventController eventController;
    RoomController roomController;
    ConferenceController conferenceController;
    UserController userController;

    UUID userUUID;
    UUID conferenceUUID;

    String role;

    ConferenceGeneralPresenter(IFrame mainFrame, IConferenceGeneralView conferenceGeneralView, UUID conferenceUUID) {

        this.conferenceUUID = conferenceUUID;
        this.conferenceGeneralView = conferenceGeneralView;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conferenceController = controllerBundle.getConferenceController();
        userController = controllerBundle.getUserController();
        eventController = controllerBundle.getEventController();
        roomController = controllerBundle.getRoomController();

        userUUID = userController.getCurrentUser();

        updateRole();
        updateGeneralData();
    }

    private void updateRole() {
        if (conferenceController.isOrganizer(conferenceUUID, userUUID, userUUID)) {
            role = "Organizer";
        } else if (conferenceController.isSpeaker(conferenceUUID, userUUID, userUUID)) {
            role = "Speaker";
        } else {
            role = "Attendee";
        }
    }

    private void updateGeneralData() {

        boolean isSpeaker = conferenceController.isSpeaker(conferenceUUID, userUUID, userUUID);

        String[][] tableData = {
                { "Conference Name", conferenceController.getConferenceName(conferenceUUID) },
                { "Start", conferenceController.getConferenceTimeRange(conferenceUUID).getStart().toString() },
                { "End", conferenceController.getConferenceTimeRange(conferenceUUID).getEnd().toString() },
                { "UUID", conferenceUUID.toString() },
                {},
                { "# Rooms", "" + roomController.getRooms(conferenceUUID, userUUID).size() },
                { "# Events", "" + eventController.getEvents(conferenceUUID, userUUID).size() },
                {},
                { "# Attendees", "" + conferenceController.getAttendees(conferenceUUID, userUUID).size() },
                { "# Speakers", "" + conferenceController.getSpeakers(conferenceUUID, userUUID).size() },
                { "# Organizers", "" + conferenceController.getOrganizers(conferenceUUID, userUUID).size() },
                {},
                { "Your role", role },
                { "# Events you're registered in", "" + eventController.getAttendeeEvents(conferenceUUID, userUUID).size() },
                { "# Events you're speaking at", isSpeaker ? ("" + eventController.getSpeakerEvents(conferenceUUID, userUUID).size()) : "N/A" }
        };

        String[] columnNames = {
                "Label",
                "Value"
        };

        TableModel tableModel = new DefaultTableModel(tableData, columnNames);

        conferenceGeneralView.setTableModel(tableModel);
    }
}
