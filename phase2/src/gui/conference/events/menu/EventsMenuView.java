package gui.conference.events.menu;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class EventsMenuView implements IPanel, IEventsMenuView {
    private JPanel panel1;
    private JButton createEventButton;
    private JList eventsList;
    private JSplitPane eventSplitPane;

    private EventsMenuPresenter eventsMenuPresenter;

    public EventsMenuView(IFrame mainFrame, UUID conferenceUUID, Supplier<Set<UUID>> getEvents, UUID defaultEventUUID, Map<String, Object> initializationArguments){
        eventsMenuPresenter = new EventsMenuPresenter(mainFrame, this, conferenceUUID, getEvents, defaultEventUUID, initializationArguments);

        eventsList.addListSelectionListener((e) -> eventsMenuPresenter.selectEventPanel(eventsList.getSelectedIndex()));
        createEventButton.addActionListener((e) -> eventsMenuPresenter.createEvent());
    }

    @Override
    public void setCreateEventButtonEnabled(boolean state) {
        createEventButton.setEnabled(state);
    }

    @Override
    public void setEventList(String[] eventNames) {
        eventsList.setListData(eventNames);
    }

    @Override
    public void setEventListSelection(int selectionIndex) {
        eventsList.setSelectedIndex(selectionIndex);
    }

    @Override
    public void setEventTabs(IPanel tabsPanel) {
        eventSplitPane.setRightComponent(tabsPanel.getPanel());
    }

    @Override
    public JPanel getPanel() {
        return panel1;
    }
}
