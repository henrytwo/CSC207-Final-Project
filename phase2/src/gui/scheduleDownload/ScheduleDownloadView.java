package gui.scheduleDownload;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ScheduleDownloadView implements IPanel, IScheduleDownloadView {
    private JButton dateScheduleDownloadButton;
    private JButton speakerScheduleDownloadButton;
    private JButton registeredScheduleDownlaodButton;
    private JTextField speakerNameTextField;
    private JTextField dateTextField;
    private JPanel ScheduleDownloadPanel;
    private JTabbedPane tabbedPane1;
    private ScheduleDownloadPresenter scheduleDownloadPresenter;

    public ScheduleDownloadView(IFrame mainFrame) {
        scheduleDownloadPresenter = new ScheduleDownloadPresenter(mainFrame, this);
        dateScheduleDownloadButton.addActionListener((e -> scheduleDownloadPresenter.printSchedule("date", getDate())));
        registeredScheduleDownlaodButton.addActionListener((e) -> scheduleDownloadPresenter.printSchedule("registered"));
        speakerScheduleDownloadButton.addActionListener((e) -> scheduleDownloadPresenter.printSchedule("speaker", getSpeakerName()));
    }

    @Override
    public JPanel getPanel() {
        return ScheduleDownloadPanel;
    }

    public String getSpeakerName() {return speakerNameTextField.getText();}

    public LocalDate getDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate localDate = LocalDate.parse(dateTextField.getText(), formatter);
        return localDate;
    }

}
