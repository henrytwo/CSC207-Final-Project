package gui.conference.general;

import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.exception.LoneOrganizerException;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import user.UserController;
import util.ControllerBundle;

import java.util.HashMap;
import java.util.UUID;

class ConferenceGeneralPresenter {

    IDialogFactory dialogFactory;
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

        dialogFactory = mainFrame.getDialogFactory();

        userUUID = userController.getCurrentUser();

        updateRole();
        updateGeneralData();
    }

    void leaveConference() {

        // As a god user, you can't technically "leave" conferences, since your role is determined outside the scope
        // of a conference in the user manager.
        if (userController.getUserIsGod(userUUID)) {
            IDialog cannotLeaveDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Unable to leave conference. God users are by definition organizers of all conferences in the system.");
                    put("title", "Error");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            cannotLeaveDialog.show();
        } else {
            IDialog confirmLeaveDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                {
                    put("message", String.format("Are you sure you want to leave this conference? (%s)", conferenceController.getConferenceName(conferenceUUID)));
                    put("title", "Confirm leave conference");
                    put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                    put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                }
            });

            if ((boolean) confirmLeaveDialog.show()) {
                try {
                    conferenceController.leaveConference(conferenceUUID, userUUID, userUUID);
                } catch (LoneOrganizerException e) {
                    IDialog loneOrganizerDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                        {
                            put("message", "Unable to leave conference. There must be at least one organizer left in the conference.");
                            put("title", "Error");
                            put("messageType", DialogFactoryOptions.dialogType.ERROR);
                        }
                    });

                    loneOrganizerDialog.show();
                }
            }
        }

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
                {"Conference Name", conferenceController.getConferenceName(conferenceUUID)},
                {"Start", conferenceController.getConferenceTimeRange(conferenceUUID).getStart().toString()},
                {"End", conferenceController.getConferenceTimeRange(conferenceUUID).getEnd().toString()},
                {"UUID", conferenceUUID.toString()},
                {},
                {"# Rooms", "" + roomController.getRooms(conferenceUUID, userUUID).size()},
                {"# Events", "" + eventController.getEvents(conferenceUUID, userUUID).size()},
                {},
                {"# Attendees", "" + conferenceController.getAttendees(conferenceUUID, userUUID).size()},
                {"# Speakers", "" + conferenceController.getSpeakers(conferenceUUID, userUUID).size()},
                {"# Organizers", "" + conferenceController.getOrganizers(conferenceUUID, userUUID).size()},
                {},
                {"Your role", role},
                {"# Events you're registered in", "" + eventController.getAttendeeEvents(conferenceUUID, userUUID).size()},
                {"# Events you're speaking at", isSpeaker ? ("" + eventController.getSpeakerEvents(conferenceUUID, userUUID).size()) : "N/A"}
        };

        String[] columnNames = {
                "Label",
                "Value"
        };

        conferenceGeneralView.setTableData(tableData, columnNames);
    }
}
