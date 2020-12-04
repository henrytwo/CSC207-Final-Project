package gui.conference;

import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import user.UserController;
import util.ControllerBundle;

import java.util.UUID;

public abstract class AbstractConferencePresenter {

    protected IFrame mainFrame;
    protected IDialogFactory dialogFactory;
    protected IPanelFactory panelFactory;

    protected EventController eventController;
    protected RoomController roomController;
    protected ConferenceController conferenceController;
    protected UserController userController;

    protected UUID signedInUserUUID;
    protected UUID conferenceUUID;

    protected String role;

    public AbstractConferencePresenter(IFrame mainFrame, UUID conferenceUUID) {
        this.mainFrame = mainFrame;
        this.conferenceUUID = conferenceUUID;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conferenceController = controllerBundle.getConferenceController();
        userController = controllerBundle.getUserController();
        eventController = controllerBundle.getEventController();
        roomController = controllerBundle.getRoomController();

        dialogFactory = mainFrame.getDialogFactory();
        panelFactory = mainFrame.getPanelFactory();

        signedInUserUUID = userController.getCurrentUser();

        updateRole();
    }

    private void updateRole() {
        if (conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID)) {
            role = "Organizer";
        } else if (conferenceController.isSpeaker(conferenceUUID, signedInUserUUID, signedInUserUUID)) {
            role = "Speaker";
        } else {
            role = "Attendee";
        }
    }
}