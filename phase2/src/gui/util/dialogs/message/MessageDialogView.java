package gui.util.dialogs.message;

import gui.util.enums.Names;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;

public class MessageDialogView implements IDialog {
    private IFrame mainFrame;
    private String message;
    private String title;
    private int messageType;

    public MessageDialogView(IFrame mainFrame, String message, String title, Names.dialogType messageType) {
        this.mainFrame = mainFrame;
        this.message = message;
        this.title = title != null ? title : "Message";

        switch (messageType) {
            case ERROR:
                this.messageType = 0;
                break;
            case WARNING:
                this.messageType = 2;
                break;
            case QUESTION:
                this.messageType = 3;
                break;
            case INFORMATION:
                this.messageType = 1;
                break;
            default:
                this.messageType = -1;
                break;
        }
    }

    @Override
    public Object show() {
        JOptionPane.showMessageDialog(mainFrame.getFrame(), message, title, messageType);

        return null;
    }
}
