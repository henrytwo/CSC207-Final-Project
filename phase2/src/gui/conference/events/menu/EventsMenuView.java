package gui.conference.events.menu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * Event Selection menu.
 */
public class EventsMenuView implements IPanel, IEventsMenuView {
    private JPanel panel1;
    private JButton createEventButton;
    private JList eventsList;
    private JSplitPane eventSplitPane;

    private EventsMenuPresenter eventsMenuPresenter;

    /**
     * @param mainFrame               main gui frame
     * @param conferenceUUID          UUID of the associated conference
     * @param getEvents               lambda function returning the set of all events.
     * @param defaultEventUUID        UUID of the default event to selected, if none provided first one is selected
     * @param initializationArguments HashMap of value to set initial state of the menu
     */
    public EventsMenuView(IFrame mainFrame, UUID conferenceUUID, Supplier<Set<UUID>> getEvents, UUID defaultEventUUID, Map<String, Object> initializationArguments) {
        eventsMenuPresenter = new EventsMenuPresenter(mainFrame, this, conferenceUUID, getEvents, defaultEventUUID, initializationArguments);

        eventsList.addListSelectionListener((e) -> eventsMenuPresenter.selectEventPanel(eventsList.getSelectedIndex()));
        createEventButton.addActionListener((e) -> eventsMenuPresenter.createEvent());
    }

    /**
     * Sets the create event button to be enables or not
     *
     * @param state boolean value, true if enabled, false if not
     */
    @Override
    public void setCreateEventButtonEnabled(boolean state) {
        createEventButton.setEnabled(state);
    }

    /**
     * Sets the list of events visible to the user
     *
     * @param eventNames names of the events in the list
     */
    @Override
    public void setEventList(String[] eventNames) {
        eventsList.setListData(eventNames);
    }

    /**
     * Sets what the current selected event appears to be
     *
     * @param selectionIndex index of the event appearing to be selected
     */
    @Override
    public void setEventListSelection(int selectionIndex) {
        eventsList.setSelectedIndex(selectionIndex);
    }

    /**
     * Sets the right panel of the events.
     *
     * @param tabsPanel panel with which to fill the right pane
     */
    @Override
    public void setEventTabs(IPanel tabsPanel) {
        eventSplitPane.setRightComponent(tabsPanel.getPanel());
    }

    /**
     * Returns the events menu.
     *
     * @return the events main panel
     */
    @Override
    public JPanel getPanel() {
        return panel1;
    }
}
