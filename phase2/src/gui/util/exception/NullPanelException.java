package gui.util.exception;

import gui.util.enums.Names;

public class NullPanelException extends RuntimeException {
    public NullPanelException(Names.panelNames name) {
        super(String.format("PanelFactory is not able to create the panel \"%s\" because it doesn't exist.", name));
    }
}
