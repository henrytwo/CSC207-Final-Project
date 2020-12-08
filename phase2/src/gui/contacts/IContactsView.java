package gui.contacts;

/**
 * interface for the contacts view of the GUI
 */
public interface IContactsView {
    void setContactsList(String[] contacts);

    void setRequestsList(String[] requests);

    void setContactsListSelection(int selectionIndex);

    void setRequestsListSelection(int selectionIndex);

    int getRequestListIndex();

    int getContactListIndex();
}
