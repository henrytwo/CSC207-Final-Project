package gui.util.interfaces;

import util.ControllerBundle;

public interface IFrame {
    void setPanel(IPanel panel);

    ControllerBundle getControllerBundle();
    IPanelFactory getPanelFactory();
}
