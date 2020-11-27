package gui.conference.menu;

import convention.ConferenceController;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.*;
import user.UserController;
import util.ControllerBundle;

import java.util.*;

class ConferenceMenuPresenter {
    private IConferenceMenuView conferenceMenuView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;
    private IDialogFactory dialogFactory;

    private ConferenceController conferenceController;
    private UserController userController;

    private List<UUID> conferenceUUIDs;
    private UUID userUUID;

    private int currentConferenceIndex = -1;

    /**
     * @param mainFrame
     * @param conferenceMenuView
     * @param defaultConferenceUUID UUID of the default conference to select. If none selected, or invalid, the first one will be selected.
     */
    ConferenceMenuPresenter(IFrame mainFrame, IConferenceMenuView conferenceMenuView, UUID defaultConferenceUUID) {
        this.conferenceMenuView = conferenceMenuView;
        this.mainFrame = mainFrame;

        panelFactory = mainFrame.getPanelFactory();
        dialogFactory = mainFrame.getDialogFactory();

        // Init controllers
        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conferenceController = controllerBundle.getConferenceController();
        userController = controllerBundle.getUserController();

        // Fetch initial data
        userUUID = userController.getCurrentUser();

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
            selectConferencePanel(defaultConferenceIndex); // this one actually sets the right hand panel
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
        Set<UUID> availableConferenceUUIDs = conferenceController.getNotUserConferences(userUUID);

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
                conferenceController.addAttendee(selectedConferenceUUID, userUUID);

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
        // Don't need to perform an update if we're already selected
        if (index != currentConferenceIndex) {
            currentConferenceIndex = index;
            UUID selectedConferenceUUID = conferenceUUIDs.get(index);

            // Update UI with tabs for this conference
            IPanel conferenceTabsPanel = panelFactory.createPanel(PanelFactoryOptions.panelNames.CONFERENCE_TABS, new HashMap<String, Object>() {
                {
                    put("conferenceUUID", selectedConferenceUUID);
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
        conferenceUUIDs = new ArrayList<>(conferenceController.getUserConferences(userUUID));
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
