package gui.contacts;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class ContactsView implements IPanel, IContactsView {
    private JPanel contactsPanel;
    private JButton deleteButton;
    private JList contactsList;
    private JButton sendRequestButton;
    private JButton rejectRequestButton;
    private JButton acceptRequestButton;
    private JList requestsList;

    private ContactsPresenter contactsPresenter;

    /**
     * Constructor fot contacts UI view.
     *
     * @param mainFrame the mainFrame of the GUI
     */
    public ContactsView(IFrame mainFrame) {
        contactsPresenter = new ContactsPresenter(mainFrame, this);

        sendRequestButton.addActionListener((e) -> contactsPresenter.sendRequest());
        deleteButton.addActionListener((e) -> contactsPresenter.deleteContact());

        acceptRequestButton.addActionListener((e) -> contactsPresenter.acceptRequest());
        rejectRequestButton.addActionListener((e) -> contactsPresenter.rejectRequest());
    }

    /**
     * Gets the panel for contacts view.
     *
     * @return contacts view panel.
     */
    @Override
    public JPanel getPanel() {
        return contactsPanel;
    }

    /**
     * Sets the contacts list.
     */
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

    @Override
    public int getRequestListIndex() {
        return requestsList.getSelectedIndex();
    }

    @Override
    public int getContactListIndex() {
        return contactsList.getSelectedIndex();
    }

}
