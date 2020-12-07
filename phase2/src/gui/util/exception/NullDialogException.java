package gui.util.exception;

import gui.util.enums.DialogFactoryOptions;

/**
 * Exception raised when an invalid dialog is requested to be created by the factory
 */
public class NullDialogException extends RuntimeException {
    public NullDialogException(DialogFactoryOptions.dialogNames name) {
        super(String.format("DialogFactory is not able to create the dialog \"%s\" because it doesn't exist.", name));
    }
}
