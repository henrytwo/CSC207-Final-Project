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
     * Constructor for contacts UI view.
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

    /**
     * sets the contact request list
     *
     * @param requests the list of requests
     */
    @Override
    public void setRequestsList(String[] requests) {
        requestsList.setListData(requests);
    }

    /**
     * sets the selection from the contacts list
     *
     * @param selectionIndex the index of the selection
     */
    @Override
    public void setContactsListSelection(int selectionIndex) {
        contactsList.setSelectedIndex(selectionIndex);
    }

    /**
     * sets the request lists selection
     *
     * @param selectionIndex the index of the selection
     */
    @Override
    public void setRequestsListSelection(int selectionIndex) {
        requestsList.setSelectedIndex(selectionIndex);
    }

    /**
     * gets the request list index
     *
     * @return the index
     */
    @Override
    public int getRequestListIndex() {
        return requestsList.getSelectedIndex();
    }

    /**
     * gets the contact list index
     *
     * @return the index
     */
    @Override
    public int getContactListIndex() {
        return contactsList.getSelectedIndex();
    }

}
