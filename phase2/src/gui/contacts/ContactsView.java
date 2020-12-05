package gui.contacts;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;
import java.util.UUID;

public class ContactsView implements IPanel, IContactsView {
    private JPanel contactsPanel;
    private JButton deleteButton;
    private JList contactsList;
    private JButton sendRequestButton;
    private JButton rejectRequestButton;
    private JButton acceptRequestButton;
    private JTable table1;

    private ContactsPresenter contactsPresenter;

    public ContactsView(IFrame mainFrame, UUID contactUUID) {
        contactsPresenter = new ContactsPresenter(mainFrame, this, contactUUID);
        //contactsList.addListSelectionListener((e) -> contactsPresenter.selectContact(contactsList.getSelectedIndex()));
        sendRequestButton.addActionListener((e) -> contactsPresenter.sendRequest());
        deleteButton.addActionListener((e) -> contactsPresenter.deleteContact());
    }

    @Override
    public JPanel getPanel() {
        return contactsPanel;
    }

    @Override
    public void setContactList(String[] contacts) {
        contactsList.setListData(contacts);
    }

    @Override
    public void setContactsListSelection(int selectionIndex) {
        contactsList.setSelectedIndex(selectionIndex);
    }


}
