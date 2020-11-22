package gui.util;

import util.ControllerBundle;

public interface Frameable {
    void setPanel(Panelable panel);
    void refreshLogin();
    ControllerBundle getControllerBundle();
}
