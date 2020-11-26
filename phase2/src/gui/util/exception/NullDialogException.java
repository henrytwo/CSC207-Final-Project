package gui.util.exception;

import gui.util.enums.DialogFactoryOptions;

public class NullDialogException extends RuntimeException {
    public NullDialogException(DialogFactoryOptions.dialogNames name) {
        super(String.format("DialogFactory is not able to create the dialog \"%s\" because it doesn't exist.", name));
    }
}
