package gui.conference.settings;

import convention.ConferenceController;
import gui.conference.AbstractConferencePresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import user.UserController;
import util.ControllerBundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

class ConferenceSettingsPresenter extends AbstractConferencePresenter {

    private IConferenceSettingsView conferenceSettingsView;
    private UserController userController;
    private ConferenceController conferenceController;

    ConferenceSettingsPresenter(IFrame mainFrame, IConferenceSettingsView conferenceSettingsView, UUID conferenceUUID) {
        super(mainFrame, conferenceUUID);

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        userController = controllerBundle.getUserController();
        conferenceController = controllerBundle.getConferenceController();

        this.conferenceSettingsView = conferenceSettingsView;

        updateConferenceUsers();
    }

    void updateConferenceUsers() {
        ArrayList<UUID> userUUIDs = new ArrayList<>(conferenceController.getUsers(conferenceUUID, signedInUserUUID));

        String[] columnNames = {
                "First Name",
                "Last Name",
                "Username",
                "UUID",
                "Attendee",
                "Speaker",
                "Organizer",
                "God"
        };

        String[][] tableData = new String[userUUIDs.size()][columnNames.length];

        for (int i = 0; i < userUUIDs.size(); i++) {
            UUID targetUserUUID = userUUIDs.get(i);

            // The user might not exist in the user controller for some reason
            boolean userRecordExists = userController.isUser(targetUserUUID);

            tableData[i] = new String[]{
                    userRecordExists ? userController.getUserFirstName(targetUserUUID) : "N/A",
                    userRecordExists ? userController.getUserLastName(targetUserUUID) : "N/A",
                    userRecordExists ? userController.getUserUsername(targetUserUUID) : "N/A",
                    targetUserUUID.toString(),
                    conferenceController.isAttendee(conferenceUUID, targetUserUUID, targetUserUUID) ? "YES" : "NO",
                    conferenceController.isSpeaker(conferenceUUID, targetUserUUID, targetUserUUID) ? "YES" : "NO",
                    conferenceController.isOrganizer(conferenceUUID, targetUserUUID, targetUserUUID) ? "YES" : "NO",
                    userRecordExists && userController.getUserIsGod(targetUserUUID) ? "YES" : "NO"
            };
        }

        conferenceSettingsView.setUserList(tableData, columnNames);

    }

    void deleteConference() {
        IDialog confirmDeleteDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
            {
                put("message", String.format("Are you sure you want to DELETE this conference? You CANNOT undo this. (%s)", conferenceController.getConferenceName(conferenceUUID)));
                put("title", "Confirm delete conference");
                put("messageType", DialogFactoryOptions.dialogType.WARNING);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        });

        if ((Boolean) confirmDeleteDialog.run()) {
            conferenceController.deleteConference(conferenceUUID, signedInUserUUID);

            // Reload the main menu to update changes
            mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU));
        }
    }


    void editConference() {
        IDialog conferenceFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFERENCE_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", conferenceUUID);
            }
        });

        if (conferenceFormDialog.run() != null) {
            // Reload the main menu to update changes
            mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU, new HashMap<String, Object>() {
                {
                    put("defaultConferenceUUID", conferenceUUID);
                }
            }));
        }
    }

}
