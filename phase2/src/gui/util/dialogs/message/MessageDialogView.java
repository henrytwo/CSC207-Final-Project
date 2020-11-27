package gui.util.dialogs.message;

import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;
import gui.util.swing.ConstantsTranslator;

import javax.swing.*;

public class MessageDialogView implements IDialog {
    private IFrame mainFrame;
    private String message;
    private String title;
    private int dialogType;

    public MessageDialogView(IFrame mainFrame, String message, String title, DialogFactoryOptions.dialogType dialogType) {
        this.mainFrame = mainFrame;
        this.message = message;
        this.title = title;

        ConstantsTranslator constantsTranslator = new ConstantsTranslator();

        this.dialogType = constantsTranslator.translateDialogType(dialogType);
    }

    @Override
    public Object run() {
        JOptionPane.showMessageDialog(mainFrame.getFrame(), message, title, dialogType);

        return null;
    }
}
