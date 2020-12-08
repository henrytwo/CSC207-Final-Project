package gui.util.dialogs;

import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import gui.util.swing.ConstantsTranslator;

import javax.swing.*;

/**
 * Dialog with message
 */
public class MessageDialogView implements IDialog {
    private IFrame mainFrame;
    private String message;
    private String title;
    private int dialogType;

    /**
     * @param mainFrame  main GUI frame
     * @param message    message to display
     * @param title      title of the dialog
     * @param dialogType icon to display
     */
    public MessageDialogView(IFrame mainFrame, String message, String title, DialogFactoryOptions.dialogType dialogType) {
        this.mainFrame = mainFrame;
        this.message = message;
        this.title = title;

        ConstantsTranslator constantsTranslator = new ConstantsTranslator();

        this.dialogType = constantsTranslator.translateDialogType(dialogType);
    }

    /**
     * Display the dialog
     *
     * @return
     */
    @Override
    public Object run() {
        JOptionPane.showMessageDialog(mainFrame.getFrame(), message, title, dialogType);

        return null;
    }
}
