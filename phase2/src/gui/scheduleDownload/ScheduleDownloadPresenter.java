package gui.scheduleDownload;

import convention.exception.InvalidSortMethodException;
import convention.schedule.ScheduleConstants;
import gateway.exceptions.PrinterException;
import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

class ScheduleDownloadPresenter extends AbstractPresenter {
    private IScheduleDownloadView scheduleDownloadView;
    private UUID selectedSpeakerUUID;

    ScheduleDownloadPresenter(IFrame mainFrame, IScheduleDownloadView scheduleDownloadView) {
        super(mainFrame);
        this.scheduleDownloadView = scheduleDownloadView;
    }

    void chooseSpeaker() {
        IDialog speakerPickerDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Choose a speaker");
                put("availableUserUUIDs", userController.getUsers());
            }
        });

        UUID newSpeakerUUID = (UUID) speakerPickerDialog.run();

        if (newSpeakerUUID != null) {
            selectedSpeakerUUID = newSpeakerUUID;
            scheduleDownloadView.setSpeakerName("Selected Speaker: " + userController.getUserFullName(newSpeakerUUID));
        }
    }

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

    void printSchedule(String sortBy, Object object) {

    }

    void printSchedule(String sortBy) {
        UUID s = signedInUserUUID;

    }
}
