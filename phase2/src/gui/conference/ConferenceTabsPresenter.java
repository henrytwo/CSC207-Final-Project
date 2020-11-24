package gui.conference;

import convention.ConferenceController;
import gui.util.enums.PanelNames;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;
import java.util.UUID;

import java.util.HashMap;

public class ConferenceTabsPresenter {
    private IPanelFactory panelFactory;

    private ConferenceController conferenceController;
    private UserController userController;

    private UUID conferenceUUID;
    private UUID userUUID;

    private boolean hasAttendeePermissions;
    private boolean hasSpeakerPermissions;
    private boolean hasOrganizerPermissions;

    private IFrame mainFrame;
    private IConferenceTabsView conferenceTabsView;

    ConferenceTabsPresenter(IFrame mainFrame, IConferenceTabsView conferenceTabsView) {
        this.mainFrame = mainFrame;
        this.conferenceTabsView = conferenceTabsView;

        panelFactory = mainFrame.getPanelFactory();
        ControllerBundle controllerBundle = mainFrame.getControllerBundle();

        conferenceController = controllerBundle.getConferenceController();
        userController = controllerBundle.getUserController();

        conferenceUUID = conferenceTabsView.getConferenceUUID();
        userUUID = userController.getCurrentUser();

        hasOrganizerPermissions = conferenceController.isOrganizer(conferenceUUID, userUUID, userUUID);
        hasSpeakerPermissions = conferenceController.isSpeaker(conferenceUUID, userUUID, userUUID) || hasOrganizerPermissions;
        hasAttendeePermissions = conferenceController.isAttendee(conferenceUUID, userUUID, userUUID) || hasSpeakerPermissions;

        updateTabs();
    }

    private void updateTabs() {
        if (hasAttendeePermissions) {
            IPanel generalView = panelFactory.createPanel(PanelNames.names.CONFERENCE_GENERAL, new HashMap<>() {
                {
                    put("conferenceUUID", conferenceTabsView.getConferenceUUID());
                    put("conferenceTabsView", conferenceTabsView);
                    /**
                     * TODO: Figure out how to deal with parent panels more orderly
                     */
                }
            });

            conferenceTabsView.setGeneralTabPanel(generalView);
        } else {
            conferenceTabsView.setTabEnabled(0, false); // Disable general tab
            conferenceTabsView.setTabEnabled(1, false); // Disable events tab
            conferenceTabsView.setTabEnabled(2, false); // Disable registered events tab
        }

        if (hasSpeakerPermissions) {

        } else {
            conferenceTabsView.setTabEnabled(3, false); // Disable speaker events tab
        }

        if (hasOrganizerPermissions) {

        } else {
            conferenceTabsView.setTabEnabled(4, false); // Disable rooms tab
            conferenceTabsView.setTabEnabled(5, false); // Disable settings tab
        }


    }
}
