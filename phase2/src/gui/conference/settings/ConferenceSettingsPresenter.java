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

    void editConference() {
        IDialog conferenceFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFERENCE_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", conferenceUUID);
            }
        });

        if (conferenceFormDialog.run() != null) {
            // Reload the main menu to update changes
            mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU));
        }
    }

}
