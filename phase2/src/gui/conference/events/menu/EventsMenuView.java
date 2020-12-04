package gui.conference.events.menu;

import gui.conference.events.menu.IEventsMenuView;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.Map;
import java.util.UUID;

public class EventsMenuView implements IPanel, IEventsMenuView {
    private JPanel panel1;
    private JButton createEventButton;
    private JList eventsList;
    private JSplitPane eventSplitPane;
    private JTable table1;
    private JButton registerEventButton;
    private JButton editEventButton;
    private JButton deleteEventButton;

    public EventsMenuView(IFrame mainFrame, UUID defaultEventUUID, Map<String, Object> initializationArguments){

    }

    @Override
    public void setEventList(String[] eventNames) {

    }

    @Override
    public void setEventListSelection(int selectionIndex) {

    }

    @Override
    public void setEventPane(IPanel tabsPanel) {

    }

    @Override
    public JPanel getPanel() {
        return null;
    }
}
