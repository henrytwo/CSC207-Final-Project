package gui.scheduleDownload;

import gui.util.AbstractPresenter;
import gui.util.interfaces.IFrame;

import java.util.UUID;

public class ScheduleDownloadPresenter extends AbstractPresenter {
    private IScheduleDownloadView scheduleDownloadView;

    ScheduleDownloadPresenter(IFrame mainFrame, IScheduleDownloadView scheduleDownloadView) {
        super(mainFrame);
        this.scheduleDownloadView = scheduleDownloadView;
    }

    void printSchedule(String sortBy, Object object) {

    }

    void printSchedule(String sortBy) {
        UUID s = signedInUserUUID;

    }
}
