package gui;

import gui.util.enums.PanelNames;
import gui.util.factories.PanelFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;
import util.ControllerBundle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Stores the primary JFrame and the necessary tools to navigate between pages
 */
public class MainFrame implements IFrame {
    private ControllerBundle controllerBundle;

    private Runnable shutdown;
    private JFrame frame;
    private IPanelFactory panelFactory;

    private final int initialWidth = 1100;
    private final int initialHeight = 700;

    /**
     * Constructs the main UI system.
     *
     * @param controllerBundle contains all of the controllers that the UI needs to interact with
     * @param shutdown         runnable that is executed when JFrame is shut down
     */
    public MainFrame(ControllerBundle controllerBundle, Runnable shutdown) {
        this.controllerBundle = controllerBundle;
        this.shutdown = shutdown;

        panelFactory = new PanelFactory(this);
    }

    /**
     * Gets the controller bundle
     *
     * @return the controller bundle
     */
    @Override
    public ControllerBundle getControllerBundle() {
        return controllerBundle;
    }

    /**
     * Sets the current panel
     *
     * @param newPanel the new panel to load
     */
    @Override
    public void setPanel(IPanel newPanel) {
        frame.setContentPane(newPanel.getMainMenuPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Get the panel factory initialized for this IFrame
     *
     * @return panel factory initialized for this IFrame
     */
    @Override
    public IPanelFactory getPanelFactory() {
        return panelFactory;
    }

    /**
     * Runs the main UI loop
     * <p>
     * If the user is not logged in, present loginUI/register prompts. Otherwise, send them to the main menu.
     */
    public void run() {
        frame = new JFrame("Bad LinkedIn Clone");

        // Adds listener to run shutdown sequence
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown.run();
            }
        });

        // Restrict size
        Dimension initialDimension = new Dimension(initialWidth, initialHeight);

        frame.setMinimumSize(initialDimension);

        // Open panel depending on login state
        if (controllerBundle.getUserController().getCurrentUser() != null) {
            setPanel(panelFactory.createPanel(PanelNames.names.MAIN_MENU));
        } else {
            setPanel(panelFactory.createPanel(PanelNames.names.LOGIN));
        }
    }
}
