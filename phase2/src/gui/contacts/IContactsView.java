package gui.contacts;

public interface IContactsView {
    void setContactsList(String[] contacts);

    void setRequestsList(String[] requests);

    void setContactsListSelection(int selectionIndex);

    void setRequestsListSelection(int selectionIndex);
}
