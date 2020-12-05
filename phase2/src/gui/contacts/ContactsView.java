package gui.contacts;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class ContactsView implements IPanel, IContactsView {
    private JPanel contactsPanel;
    private JButton deleteButton;
    private JList list1;
    private JButton sendRequestButton;
    private JButton rejectRequestButton;
    private JButton acceptRequestButton;
    private JTable table1;
    private ContactsPresenter contactsPresenter;

    public ContactsView(IFrame mainFrame) {

    }

    @Override
    public JPanel getPanel() {
        return contactsPanel;
    }

    @Override
    public void setContactsList(String[] contacts) {
    }
}
