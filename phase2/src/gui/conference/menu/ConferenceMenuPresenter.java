package gui.conference.menu;

import convention.ConferenceController;
import gui.util.enums.Names;
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

    ConferenceMenuPresenter(IFrame mainFrame, IConferenceMenuView conferenceMenuView) {
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

            // Set initial conference selection
            conferenceMenuView.setConferenceListSelection(0);
            selectConferencePanel(0);
        }
    }

    void createConference() {

    }

    void joinConference() {
        Set<UUID> availableConferenceUUIDs = conferenceController.getNotUserConferences(userUUID);

        if (availableConferenceUUIDs.size() == 0) {
            IDialog noConferenceDialog = dialogFactory.createDialog(Names.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "There are no conferences available for you to join.");
                    put("messageType", Names.dialogType.ERROR);
                }
            });

            noConferenceDialog.show();
        } else {
            IDialog conferencePicker = dialogFactory.createDialog(Names.dialogNames.CONFERENCE_PICKER, new HashMap<String, Object>() {
                {
                    put("instructions", "Choose a conference to join");
                    put("availableConferenceUUIDs", availableConferenceUUIDs);
                }
            });

            // IDialog returns Object type by default, so we have to cast
            UUID selectedConferenceUUID = (UUID) conferencePicker.show();

            if (selectedConferenceUUID != null) {
                conferenceController.addAttendee(selectedConferenceUUID, userUUID);

                // Update the local list with the new conference
                updateConferenceList();
                updateConferenceNames();

                // Select the latest conference
                int index = conferenceUUIDs.indexOf(selectedConferenceUUID);

                conferenceMenuView.setConferenceListSelection(index);
                selectConferencePanel(index);
            }
        }
    }

    /**
     * Updates the panel on the right side of the screen with the currently selected conference
     *
     * @param index
     */
    void selectConferencePanel(int index) {
        // Don't need to perform an update if we're already selected
        if (index != currentConferenceIndex) {
            currentConferenceIndex = index;
            UUID selectedConferenceUUID = conferenceUUIDs.get(index);

            // Update UI with tabs for this conference
            IPanel conferenceTabsPanel = panelFactory.createPanel(Names.panelNames.CONFERENCE_TABS, new HashMap<String, Object>() {
                {
                    put("conferenceUUID", selectedConferenceUUID);
                }
            });

            conferenceMenuView.setConferenceTabs(conferenceTabsPanel);
        }
    }

    /**
     * Update the local host of conference UUIDs
     */
    private void updateConferenceList() {
        currentConferenceIndex = -1;
        conferenceUUIDs = new ArrayList<>(conferenceController.getUserConferences(userUUID));
    }

    private void updateConferenceNames() {
        String[] conferenceNames = new String[conferenceUUIDs.size()];

        for (int i = 0; i < conferenceUUIDs.size(); i++) {
            conferenceNames[i] = conferenceController.getConferenceName(conferenceUUIDs.get(i));
        }

        conferenceMenuView.setConferenceList(conferenceNames);
    }
}
