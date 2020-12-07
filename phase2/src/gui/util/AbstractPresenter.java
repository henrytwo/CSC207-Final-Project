package gui.util;

import contact.ContactController;
import convention.ConferenceController;
import convention.EventController;
import convention.RoomController;
import convention.ScheduleController;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import messaging.ConversationController;
import user.UserController;
import util.ControllerBundle;

import java.util.UUID;

/**
 * Abstract class for presenters containing the most commonly used fields
 */
public abstract class AbstractPresenter {

    protected IFrame mainFrame;
    protected IDialogFactory dialogFactory;
    protected IPanelFactory panelFactory;

    protected EventController eventController;
    protected RoomController roomController;
    protected ConferenceController conferenceController;
    protected UserController userController;
    protected ContactController contactController;
    protected ConversationController conversationController;
    protected ScheduleController scheduleController;

    protected UUID signedInUserUUID;

    /**
     * @param mainFrame main GUI frame
     */
    protected AbstractPresenter(IFrame mainFrame) {
        this.mainFrame = mainFrame;

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        conferenceController = controllerBundle.getConferenceController();
        userController = controllerBundle.getUserController();
        eventController = controllerBundle.getEventController();
        roomController = controllerBundle.getRoomController();
        conversationController = controllerBundle.getConversationController();
        contactController = controllerBundle.getContactController();
        scheduleController = controllerBundle.getScheduleController();

        dialogFactory = mainFrame.getDialogFactory();
        panelFactory = mainFrame.getPanelFactory();

        signedInUserUUID = userController.getCurrentUser();
    }
}
