package gui.conference.util;

import gui.util.AbstractPresenter;
import gui.util.interfaces.IFrame;

import java.util.UUID;

/**
 * Abstract class for presenters of tabs that are associated with a specific conference.
 */
public abstract class AbstractConferencePresenter extends AbstractPresenter {

    protected UUID conferenceUUID;
    protected String role;

    /**
     * @param mainFrame      main GUI frame
     * @param conferenceUUID UUID of the associated conference
     */
    protected AbstractConferencePresenter(IFrame mainFrame, UUID conferenceUUID) {
        super(mainFrame);
        this.mainFrame = mainFrame;
        this.conferenceUUID = conferenceUUID;

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