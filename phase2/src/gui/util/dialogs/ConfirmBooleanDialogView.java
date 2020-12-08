package gui.util.dialogs;

import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import gui.util.swing.ConstantsTranslator;

import javax.swing.*;

/**
 * Generic boolean confirmation dialog
 */
public class ConfirmBooleanDialogView implements IDialog {
    private IFrame mainFrame;
    private String message;
    private String title;
    private int optionType;
    private int dialogType;

    /**
     * @param mainFrame  main GUI frame
     * @param message    message to display
     * @param title      title of the dialog
     * @param dialogType icon to display
     * @param optionType buttons to present
     */
    public ConfirmBooleanDialogView(IFrame mainFrame, String message, String title, DialogFactoryOptions.dialogType dialogType, DialogFactoryOptions.optionType optionType) {
        this.mainFrame = mainFrame;
        this.message = message;
        this.title = title;

        ConstantsTranslator constantsTranslator = new ConstantsTranslator();

        this.dialogType = constantsTranslator.translateDialogType(dialogType);
        this.optionType = constantsTranslator.translateOptionType(optionType);
    }

    /**
     * Runs the dialog
     *
     * @return true iff the user selected yes
     */
    @Override
    public Boolean run() {
        return JOptionPane.showConfirmDialog(mainFrame.getFrame(), message, title, optionType, dialogType) == 0; // 0 means yes
    }
}
