package gui.util.exception;

import gui.util.enums.PanelFactoryOptions;

public class NullPanelException extends RuntimeException {
    public NullPanelException(PanelFactoryOptions.panelNames name) {
        super(String.format("PanelFactory is not able to create the panel \"%s\" because it doesn't exist.", name));
    }
}
