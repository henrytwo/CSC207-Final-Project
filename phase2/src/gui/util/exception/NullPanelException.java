package gui.util.exception;

import gui.util.enums.PanelNames;

public class NullPanelException extends RuntimeException {
    public NullPanelException(PanelNames.names name) {
        super(String.format("PanelFactory is not able to create the panel \"%s\" because it doesn't exist.", name));
    }
}
