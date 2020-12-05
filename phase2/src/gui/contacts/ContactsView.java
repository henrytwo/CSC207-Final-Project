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
    private JList requestsList;

    private ContactsPresenter contactsPresenter;

    public ContactsView(IFrame mainFrame, UUID defaultContactUUID, UUID defaultRequestUUID) {
        contactsPresenter = new ContactsPresenter(mainFrame, this, defaultContactUUID, defaultRequestUUID);
        //contactsList.addListSelectionListener((e) -> contactsPresenter.selectContact(contactsList.getSelectedIndex()));
        sendRequestButton.addActionListener((e) -> contactsPresenter.sendRequest());
        deleteButton.addActionListener((e) -> contactsPresenter.deleteContact());
        acceptRequestButton.addActionListener((e) -> contactsPresenter.acceptRequest());
        rejectRequestButton.addActionListener((e)-> contactsPresenter.rejectRequest());
        requestsList.addListSelectionListener((e) -> contactsPresenter.requestSelectionUpdate(requestsList.getSelectedIndex()));
    }

    @Override
    public JPanel getPanel() {
        return contactsPanel;
    }

    @Override
    public void setContactsList(String[] contacts) {
        contactsList.setListData(contacts);
    }

    @Override
    public void setRequestsList(String[] requests) {
        requestsList.setListData(requests);
    }

    @Override
    public void setContactsListSelection(int selectionIndex) {
        contactsList.setSelectedIndex(selectionIndex);
    }

    @Override
    public void setRequestsListSelection(int selectionIndex) {
        requestsList.setSelectedIndex(selectionIndex);
    }


}
