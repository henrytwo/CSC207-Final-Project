package gui.conference.menu;

import gui.conference.tabs.ConferenceTabsConstants;
import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import java.util.*;

/**
 * Manages the ConferenceMenuView
 */
class ConferenceMenuPresenter extends AbstractPresenter {
    private IConferenceMenuView conferenceMenuView;

    private List<UUID> conferenceUUIDs;

    private int currentConferenceIndex = -1;
    private Map<String, Object> initializationArguments;

    /**
     * @param mainFrame               main UI frame
     * @param conferenceMenuView      view that this presenter is managing
     * @param defaultConferenceUUID   UUID of the default conference to select. If none selected, or invalid, the first one will be selected.
     * @param initializationArguments HashMap of values that can be used to set the initial state of a panel
     */
    ConferenceMenuPresenter(IFrame mainFrame, IConferenceMenuView conferenceMenuView, UUID defaultConferenceUUID, Map<String, Object> initializationArguments) {
        super(mainFrame);

        this.conferenceMenuView = conferenceMenuView;
        this.initializationArguments = initializationArguments;

        updateConferenceList();

        // Make initial selection
        if (conferenceUUIDs.size() > 0) {
            updateConferenceNames();

            int defaultConferenceIndex = 0;

            // Choose the specified default conference UUID
            if (defaultConferenceUUID != null && conferenceUUIDs.contains(defaultConferenceUUID)) {
                defaultConferenceIndex = conferenceUUIDs.indexOf(defaultConferenceUUID);
            }

            // Set initial conference selection
            conferenceMenuView.setConferenceListSelection(defaultConferenceIndex); // makes it look like we select it
            selectConferencePanel(defaultConferenceIndex, (ConferenceTabsConstants.tabNames) initializationArguments.get("defaultTabName")); // this one actually sets the right hand panel
        }
    }

    /**
     * Initiates dialog for a user to create a conference
     */
    void createConference() {
        IDialog conferenceFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFERENCE_FORM);

        UUID newConferenceUUID = (UUID) conferenceFormDialog.run();

        if (newConferenceUUID != null) {
            updateAndSelectNewConference(newConferenceUUID);
        }
    }

    /**
     * Initiates dialog for a user to join a conference
     */
    void joinConference() {
        Set<UUID> availableConferenceUUIDs = conferenceController.getNotUserConferences(signedInUserUUID);

        if (availableConferenceUUIDs.size() == 0) {
            IDialog noConferenceDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "There are no conferences available for you to join.");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            noConferenceDialog.run();
        } else {
            IDialog conferencePicker = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFERENCE_PICKER, new HashMap<String, Object>() {
                {
                    put("instructions", "Choose a conference to join");
                    put("availableConferenceUUIDs", availableConferenceUUIDs);
                }
            });

            // IDialog returns Object type by default, so we have to cast
            UUID selectedConferenceUUID = (UUID) conferencePicker.run();

            if (selectedConferenceUUID != null) {
                conferenceController.addAttendee(selectedConferenceUUID, signedInUserUUID);

                updateAndSelectNewConference(selectedConferenceUUID);
            }
        }
    }

    /**
     * Updates the local list of conferences and selects a conference by UUID
     *
     * @param selectedConferenceUUID UUID of conference to open
     */
    private void updateAndSelectNewConference(UUID selectedConferenceUUID) {
        // Update the local list with the new conference
        updateConferenceList();
        updateConferenceNames();

        // Select the latest conference
        int index = conferenceUUIDs.indexOf(selectedConferenceUUID);

        conferenceMenuView.setConferenceListSelection(index);
        selectConferencePanel(index);
    }

    /**
     * Updates the panel on the right side of the screen with the currently selected conference
     *
     * @param index index of the conference to open
     */
    void selectConferencePanel(int index) {
        selectConferencePanel(index, null);
    }

    /**
     * Updates the panel on the right side of the screen with the currently selected conference
     *
     * @param index          index of the conference to open
     * @param defaultTabName name of the tab to open by default
     */
    void selectConferencePanel(int index, ConferenceTabsConstants.tabNames defaultTabName) {
        // Don't need to perform an update if we're already selected
        if (index != currentConferenceIndex) {
            currentConferenceIndex = index;
            UUID selectedConferenceUUID = conferenceUUIDs.get(index);

            // Update UI with tabs for this conference
            IPanel conferenceTabsPanel = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_TABS, new HashMap<String, Object>(initializationArguments) {
                {
                    put("conferenceUUID", selectedConferenceUUID);
                    put("defaultTabName", defaultTabName);
                }
            });

            conferenceMenuView.setConferenceTabs(conferenceTabsPanel);
        }
    }

    /**
     * Update the local list of conference UUIDs
     */
    private void updateConferenceList() {
        currentConferenceIndex = -1;
        conferenceUUIDs = new ArrayList<>(conferenceController.getUserConferences(signedInUserUUID));
    }

    /**
     * Updates the local list of conference names
     */
    private void updateConferenceNames() {
        String[] conferenceNames = new String[conferenceUUIDs.size()];

        for (int i = 0; i < conferenceUUIDs.size(); i++) {
            conferenceNames[i] = conferenceController.getConferenceName(conferenceUUIDs.get(i));
        }

        conferenceMenuView.setConferenceList(conferenceNames);
    }
}
