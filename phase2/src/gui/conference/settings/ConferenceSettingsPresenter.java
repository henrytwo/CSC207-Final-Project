package gui.conference.settings;

import gui.conference.AbstractConferencePresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.HashMap;
import java.util.UUID;

class ConferenceSettingsPresenter extends AbstractConferencePresenter {

    private IConferenceSettingsView conferenceSettingsView;

    ConferenceSettingsPresenter(IFrame mainFrame, IConferenceSettingsView conferenceSettingsView, UUID conferenceUUID) {
        super(mainFrame, conferenceUUID);

        this.conferenceSettingsView = conferenceSettingsView;
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
            conferenceController.deleteConference(conferenceUUID, userUUID);

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
