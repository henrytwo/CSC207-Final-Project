package gui.util.interfaces;

import gui.util.interfaces.IPanel;
import util.ControllerBundle;

public interface IFrame {
    void setPanel(IPanel panel);
    void refreshLogin();
    ControllerBundle getControllerBundle();
}
