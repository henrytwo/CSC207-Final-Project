package gui.util.interfaces;

import util.ControllerBundle;

import javax.swing.*;

/**
 * Generic frame interface
 */
public interface IFrame {
    void setPanel(IPanel panel);

    ControllerBundle getControllerBundle();

    IPanelFactory getPanelFactory();

    IDialogFactory getDialogFactory();

    JFrame getFrame();
}
