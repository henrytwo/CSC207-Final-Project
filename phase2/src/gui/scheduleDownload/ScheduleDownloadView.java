package gui.scheduleDownload;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

/**
 * Allows the user to export the schedule
 */
public class ScheduleDownloadView implements IPanel, IScheduleDownloadView {
    private JButton dateScheduleDownloadButton;
    private JButton speakerScheduleDownloadButton;
    private JButton registeredScheduleDownloadButton;
    private JLabel speakerName;
    private JTextField dateTextField;
    private JPanel ScheduleDownloadPanel;
    private JButton chooseSpeakerButton;
    private ScheduleDownloadPresenter scheduleDownloadPresenter;

    /**
     * Constructs ScheduleDownloadView
     * @param mainFrame
     */
    public ScheduleDownloadView(IFrame mainFrame) {
        scheduleDownloadPresenter = new ScheduleDownloadPresenter(mainFrame, this);
        dateScheduleDownloadButton.addActionListener((e) -> scheduleDownloadPresenter.printScheduleDate());
        registeredScheduleDownloadButton.addActionListener((e) -> scheduleDownloadPresenter.printScheduleRegistered());

        speakerScheduleDownloadButton.addActionListener((e) -> scheduleDownloadPresenter.printScheduleSpeaker());
        chooseSpeakerButton.addActionListener((e) -> scheduleDownloadPresenter.chooseSpeaker());
    }

    /**
     * Sets the speaker's name for sort by speaker
     *
     * @param name new name
     */
    @Override
    public void setSpeakerName(String name) {
        speakerName.setText(name);
    }

    /**
     * Returns the JPanel object
     *
     * @return
     */
    @Override
    public JPanel getPanel() {
        return ScheduleDownloadPanel;
    }

    /**
     * Gets the date for sort by date
     *
     * @return
     */
    @Override
    public String getDateString() {
        return dateTextField.getText();
    }

}
