package gui.util.exception;

import gui.util.enums.Names;

public class NullDialogException extends RuntimeException {
    public NullDialogException(Names.dialogNames name) {
        super(String.format("DialogFactory is not able to create the dialog \"%s\" because it doesn't exist.", name));
    }
}
