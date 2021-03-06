package gui.conference.form;

import convention.calendar.TimeRange;
import convention.exception.InvalidNameException;
import convention.exception.InvalidTimeRangeException;
import gui.util.AbstractPresenter;
import gui.util.date.DateParser;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Manages ConferenceFormDialog
 */
class ConferenceFormPresenter extends AbstractPresenter {

    private boolean isExistingConference;

    private IConferenceFormDialog conferenceFormDialog;

    private String conferenceName = "";
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private UUID conferenceUUID;

    private DateParser dateParser = new DateParser();

    /**
     * @param mainFrame            main GUI frame
     * @param conferenceFormDialog dialog to manage
     * @param conferenceUUID       UUID of associated conference
     */
    ConferenceFormPresenter(IFrame mainFrame, IConferenceFormDialog conferenceFormDialog, UUID conferenceUUID) {
        super(mainFrame);
        this.conferenceFormDialog = conferenceFormDialog;

        this.conferenceUUID = conferenceUUID;

        // Existing conferences will have a non-null UUID
        isExistingConference = conferenceUUID != null;

        if (isExistingConference) {
            conferenceFormDialog.setDialogTitle(String.format("Editing Conference (%s)", conferenceUUID));

            conferenceName = conferenceController.getConferenceName(conferenceUUID);
            startTime = conferenceController.getConferenceTimeRange(conferenceUUID).getStart();
            endTime = conferenceController.getConferenceTimeRange(conferenceUUID).getEnd();

            conferenceFormDialog.setName(conferenceName);
            conferenceFormDialog.setStart(dateParser.dateTimeToString(startTime));
            conferenceFormDialog.setEnd(dateParser.dateTimeToString(endTime));
        } else {
            conferenceFormDialog.setDialogTitle("Create new conference");
        }
    }

    /**
     * Submit form and:
     * 1) Apply edit changes if conference is existing, or
     * 2) Create a new conference otherwise
     */
    void submit() {
        try {
            conferenceName = conferenceFormDialog.getName();

            startTime = dateParser.stringToDateTime(conferenceFormDialog.getStart());
            endTime = dateParser.stringToDateTime(conferenceFormDialog.getEnd());

            TimeRange timeRange = new TimeRange(startTime, endTime);

            if (isExistingConference) {
                conferenceController.setConferenceName(conferenceUUID, signedInUserUUID, conferenceName);
                conferenceController.setConferenceTimeRange(conferenceUUID, signedInUserUUID, timeRange);
            } else {
                conferenceUUID = conferenceController.createConference(conferenceName, timeRange, signedInUserUUID);
            }

            // Update conference UUID in case it has changed
            conferenceFormDialog.setConferenceUUID(conferenceUUID);
            conferenceFormDialog.setUpdated(true);

            // Close the dialog so it isn't blocking anymore
            conferenceFormDialog.close();
        } catch (DateTimeParseException e) {

            IDialog dateTimeParseErrorDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", String.format("Unable to submit form: Invalid date. Please follow the given format. [%s]", dateParser.getFormat()));
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            dateTimeParseErrorDialog.run();

        } catch (InvalidTimeRangeException e) {

            IDialog invalidTimeRangeDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: Invalid date range. End time must be after start time.");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            invalidTimeRangeDialog.run();

        } catch (InvalidNameException e) {

            IDialog invalidConferenceNameDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("title", "Error");
                    put("message", "Unable to submit form: Invalid name. Conference name must be non empty.");
                    put("messageType", DialogFactoryOptions.dialogType.ERROR);
                }
            });

            invalidConferenceNameDialog.run();
        }
    }
}
