package gui.contacts;

import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

import javax.swing.*;

public class ContactsView implements IPanel, IContactsView {
    private JPanel contactsPanel;

    public ContactsView(IFrame mainFrame) {

    }

    @Override
    public JPanel getPanel() {
        return contactsPanel;
    }
}
