package gui.util.dialogs.message;

import gui.util.enums.Names;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

public class ConfirmBooleanDialogView implements IDialog {
    public ConfirmBooleanDialogView(IFrame mainFrame, String message, String title, Names.dialogType messageType) {

    }

    @Override
    public Boolean show() {

        return true;
    }
}
