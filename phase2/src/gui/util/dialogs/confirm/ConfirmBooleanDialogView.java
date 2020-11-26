package gui.util.dialogs.confirm;

import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import gui.util.swing.ConstantsTranslator;

import javax.swing.*;

public class ConfirmBooleanDialogView implements IDialog {
    private IFrame mainFrame;
    private String message;
    private String title;
    private int optionType;
    private int dialogType;

    public ConfirmBooleanDialogView(IFrame mainFrame, String message, String title, DialogFactoryOptions.dialogType dialogType, DialogFactoryOptions.optionType optionType) {
        this.mainFrame = mainFrame;
        this.message = message;
        this.title = title != null ? title : "Message";

        ConstantsTranslator constantsTranslator = new ConstantsTranslator();

        this.dialogType = constantsTranslator.translateDialogType(dialogType);
        this.optionType = constantsTranslator.translateOptionType(optionType);
    }

    @Override
    public Boolean show() {
        return JOptionPane.showConfirmDialog(mainFrame.getFrame(), message, title, optionType, dialogType) == 0; // 0 means yes
    }
}
