package gui.util.dialogs.message;

import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;

public class MessageDialogView implements IDialog {
    private IFrame mainFrame;
    private String message;
    private String title;
    private int messageType;

    public MessageDialogView(IFrame mainFrame, String message, String title, int messageType) {
        this.mainFrame = mainFrame;
        this.message = message;
        this.title = title != null ? title : "Message";
        this.messageType = messageType;
    }

    @Override
    public Object show() {
        JOptionPane.showMessageDialog(mainFrame.getFrame(), message, title, messageType);

        return null;
    }
}
