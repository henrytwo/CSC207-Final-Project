package gui.conference.general;

import convention.ConferenceController;
import gui.util.interfaces.IFrame;
import user.UserController;
import util.ControllerBundle;
import java.util.UUID;

class ConferenceGeneralPresenter {
    ConferenceGeneralPresenter(IFrame mainFrame, IConferenceGeneralView conferenceGeneralView, UUID conferenceUUID) {

        ControllerBundle controllerBundle = mainFrame.getControllerBundle();
        ConferenceController conferenceController = controllerBundle.getConferenceController();
        UserController userController = controllerBundle.getUserController();

        UUID userUUID = userController.getCurrentUser();

        String role;

        if (conferenceController.isOrganizer(conferenceUUID, userUUID, userUUID)) {
            role = "Organizer";
        } else if (conferenceController.isOrganizer(conferenceUUID, userUUID, userUUID)) {
            role = "Speaker";
        } else {
            role = "Attendee";
        }

        conferenceGeneralView.setTestText(String.format("%s %s %s %s", conferenceUUID,
                conferenceController.getConferenceName(conferenceUUID),
                conferenceController.getConferenceTimeRange(conferenceUUID),
                role));

    }
}
