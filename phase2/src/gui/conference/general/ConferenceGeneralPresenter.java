package gui.conference.general;

import convention.exception.LoneOrganizerException;
import gui.conference.AbstractConferencePresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.HashMap;
import java.util.UUID;

class ConferenceGeneralPresenter extends AbstractConferencePresenter {

    private IConferenceGeneralView conferenceGeneralView;

    ConferenceGeneralPresenter(IFrame mainFrame, IConferenceGeneralView conferenceGeneralView, UUID conferenceUUID) {
        super(mainFrame, conferenceUUID);

        this.conferenceGeneralView = conferenceGeneralView;

        updateGeneralData();
    }

    void leaveConference() {

        // As a god user, you can't technically "leave" conferences, since your role is determined outside the scope
        // of a conference in the user manager.
        if (userController.getUserIsGod(signedInUserUUID)) {
            IDialog cannotLeaveDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("message", "Unable to leave conference. God users are by definition organizers of all conferences in the system.");
                    put("title", "Error");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            cannotLeaveDialog.run();
        } else {
            IDialog confirmLeaveDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                {
                    put("message", String.format("Are you sure you want to leave this conference? (%s)", conferenceController.getConferenceName(conferenceUUID)));
                    put("title", "Confirm leave conference");
                    put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                    put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                }
            });

            if ((boolean) confirmLeaveDialog.run()) {
                try {
                    conferenceController.leaveConference(conferenceUUID, signedInUserUUID, signedInUserUUID);

                    // Reload the main menu to update changes
                    mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU));
                } catch (LoneOrganizerException e) {
                    IDialog loneOrganizerDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                        {
                            put("message", "Unable to leave conference. There must be at least one organizer left in the conference.");
                            put("title", "Error");
                            put("messageType", DialogFactoryOptions.dialogType.ERROR);
                        }
                    });

                    loneOrganizerDialog.run();
                }
            }
        }
    }

    private void updateGeneralData() {
        boolean isSpeaker = conferenceController.isSpeaker(conferenceUUID, signedInUserUUID, signedInUserUUID);

        String[][] tableData = {
                {"Conference Name", conferenceController.getConferenceName(conferenceUUID)},
                {"Start", conferenceController.getConferenceTimeRange(conferenceUUID).getStart().toString()},
                {"End", conferenceController.getConferenceTimeRange(conferenceUUID).getEnd().toString()},
                {"UUID", conferenceUUID.toString()},
                {},
                {"# Rooms", "" + roomController.getRooms(conferenceUUID, signedInUserUUID).size()},
                {"# Events", "" + eventController.getEvents(conferenceUUID, signedInUserUUID).size()},
                {},
                {"# Attendees", "" + conferenceController.getAttendees(conferenceUUID, signedInUserUUID).size()},
                {"# Speakers", "" + conferenceController.getSpeakers(conferenceUUID, signedInUserUUID).size()},
                {"# Organizers", "" + conferenceController.getOrganizers(conferenceUUID, signedInUserUUID).size()},
                {},
                {"Your role", role},
                {"# Events you're registered in", "" + eventController.getAttendeeEvents(conferenceUUID, signedInUserUUID).size()},
                {"# Events you're speaking at", isSpeaker ? ("" + eventController.getSpeakerEvents(conferenceUUID, signedInUserUUID).size()) : "N/A"}
        };

        String[] columnNames = {
                "Key",
                "Value"
        };

        conferenceGeneralView.setTableData(tableData, columnNames);
    }
}
