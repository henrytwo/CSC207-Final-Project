package gui.scheduleDownload;

import convention.exception.InvalidSortMethodException;
import convention.schedule.ScheduleConstants;
import gateway.exceptions.PrinterException;
import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Manages Schedule Download VIew
 */
class ScheduleDownloadPresenter extends AbstractPresenter {
    private IScheduleDownloadView scheduleDownloadView;
    private UUID selectedSpeakerUUID;

    /**
     * Constructs ScheduleDownloadPresenter
     *
     * @param mainFrame            main GUI frame
     * @param scheduleDownloadView view that we're managing
     */
    ScheduleDownloadPresenter(IFrame mainFrame, IScheduleDownloadView scheduleDownloadView) {
        super(mainFrame);
        this.scheduleDownloadView = scheduleDownloadView;
    }

    /**
     * Choose speaker for sort by speaker
     */
    void chooseSpeaker() {
        IDialog speakerPickerDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Choose a user to sort by\n(Note: All users in the system are included here, so they may not have assigned events)");
                put("availableUserUUIDs", userController.getUsers());
            }
        });

        UUID newSpeakerUUID = (UUID) speakerPickerDialog.run();

        if (newSpeakerUUID != null) {
            selectedSpeakerUUID = newSpeakerUUID;
            scheduleDownloadView.setSpeakerName("Selected Speaker: " + userController.getUserFullName(newSpeakerUUID));
        }
    }

    /**
     * Display generic error
     *
     * @param error error message
     */
    private void displayError(String error) {
        IDialog errorDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
            {
                put("message", error);
                put("title", "Error");
                put("messageType", DialogFactoryOptions.dialogType.ERROR);
            }
        });

        errorDialog.run();
    }

    /**
     * Sort by speaker
     */
    void printScheduleSpeaker() {
        if (selectedSpeakerUUID != null) {
            try {
                scheduleController.printSchedule(ScheduleConstants.sortByMethods.SPEAKER, new HashMap<String, Object>() {
                    {
                        put("speakerUUID", selectedSpeakerUUID);
                    }
                });
            } catch (PrinterException | IOException | InvalidSortMethodException e) {
                displayError(e.getMessage());
            }
        } else {
            displayError("You must select a speaker to download the schedule");
        }
    }

    /**
     * Sort by user's registered events
     */
    void printScheduleRegistered() {
        try {
            scheduleController.printSchedule(ScheduleConstants.sortByMethods.REGISTERED, new HashMap<String, Object>() {
                {
                    put("userUUID", signedInUserUUID);
                }
            });
        } catch (PrinterException | IOException | InvalidSortMethodException e) {
            displayError(e.getMessage());
        }
    }

    /**
     * Sort by date
     */
    void printScheduleDate() {
        String dateString = scheduleDownloadView.getDateString();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-uuuu");
            LocalDate date = LocalDate.parse(dateString, formatter);

            scheduleController.printSchedule(ScheduleConstants.sortByMethods.DATE, new HashMap<String, Object>() {
                {
                    put("date", date);
                }
            });

        } catch (DateTimeParseException e) {
            displayError("Invalid date: You must use the format (MM-dd-YYYY)");
        } catch (PrinterException | IOException | InvalidSortMethodException e) {
            displayError(e.getMessage());
        }
    }
}
