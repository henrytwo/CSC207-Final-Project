package gui.util.dialogs.confirm;

import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import javax.swing.*;

public class ConfirmBooleanDialogView implements IDialog {
    private IFrame mainFrame;
    private String message;
    private String title;
    private int optionType;
    private int messageType;

    public ConfirmBooleanDialogView(IFrame mainFrame, String message, String title, DialogFactoryOptions.dialogType messageType, DialogFactoryOptions.optionType optionType) {
        this.mainFrame = mainFrame;
        this.message = message;
        this.title = title != null ? title : "Message";

        switch (messageType) {
            case ERROR:
                this.messageType = JOptionPane.ERROR_MESSAGE;
                break;
            case WARNING:
                this.messageType = JOptionPane.WARNING_MESSAGE;
                break;
            case QUESTION:
                this.messageType = JOptionPane.QUESTION_MESSAGE;
                break;
            case INFORMATION:
                this.messageType = JOptionPane.INFORMATION_MESSAGE;
                break;
            default:
                this.messageType = JOptionPane.PLAIN_MESSAGE;
                break;
        }

        switch (optionType) {
            case YES_NO_OPTION:
                this.optionType = JOptionPane.YES_NO_OPTION;
                break;
            case YES_NO_CANCEL_OPTION:
                this.messageType = JOptionPane.YES_NO_CANCEL_OPTION;
                break;
            case OK_CANCEL_OPTION:
                this.messageType = JOptionPane.OK_CANCEL_OPTION;
                break;
            default:
                this.messageType = JOptionPane.DEFAULT_OPTION;
                break;
        }
    }

    @Override
    public Boolean show() {
        return JOptionPane.showConfirmDialog(mainFrame.getFrame(), message, title, optionType, messageType) == 0; // 0 means yes
    }
}
