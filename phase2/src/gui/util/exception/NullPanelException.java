package gui.util.exception;

public class NullPanelException extends RuntimeException {
    public NullPanelException(String name) {
        super(String.format("PanelFactory is not able to create the panel \"%s\" because it doesn't exist.", name));
    }
}
