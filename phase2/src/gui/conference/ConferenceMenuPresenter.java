package gui.conference;

import convention.ConferenceController;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class ConferenceMenuPresenter {
    private IConferenceMenuView conferenceMenuView;
    private IFrame mainFrame;

    private IPanelFactory panelFactory;

    private ConferenceController conferenceController;
    private UserController userController;

    private List<UUID> conferenceUUIDs;
    private UUID userUUID;

    private int currentConferenceIndex = -1;

    ConferenceMenuPresenter(IFrame mainFrame, IConferenceMenuView conferenceMenuView) {
        this.conferenceMenuView = conferenceMenuView;
        this.mainFrame = mainFrame;

        panelFactory = mainFrame.getPanelFactory();

        // Init controllers
        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conferenceController = controllerBundle.getConferenceController();
        userController = controllerBundle.getUserController();

        // Fetch initial data
        userUUID = userController.getCurrentUser();

        updateConferences();
    }

    void selectConference(int index) {
        // Don't need to perform an update if we're already selected
        if (index != currentConferenceIndex) {
            currentConferenceIndex = index;

            System.out.println("Selected: " + conferenceUUIDs.get(index));
        }
    }

    private void updateConferences() {
        conferenceUUIDs = new ArrayList<>(conferenceController.getUserConferences(userUUID));

        // If there are no conferences, then we should display something else
        if (conferenceUUIDs.size() == 0) {

            /**
             * TODO: Deal with no conferences
             */

        } else {
            updateConferenceNames();

            // Set initial conference selection
            conferenceMenuView.setConferenceListSelection(0);
            selectConference(0);
        }
    }

    private void updateConferenceNames() {
        String[] conferenceNames = new String[conferenceUUIDs.size()];

        for (int i = 0; i < conferenceUUIDs.size(); i++) {
            conferenceNames[i] = conferenceController.getConferenceName(conferenceUUIDs.get(i));
        }

        conferenceMenuView.setConferenceList(conferenceNames);
    }
}
