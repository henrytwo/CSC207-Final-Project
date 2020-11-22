package gui;

import util.ControllerBundle;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUISystem {
    ControllerBundle controllerBundle;

    Runnable shutdown;
    JFrame frame;

    /**
     * Constructs the main UI system.
     *
     * @param controllerBundle contains all of the controllers that the UI needs to interact with
     * @param shutdown         runnable that is executed when JFrame is shut down
     */
    public GUISystem(ControllerBundle controllerBundle, Runnable shutdown) {
        this.controllerBundle = controllerBundle;
        this.shutdown = shutdown;
    }

    /**
     * Sets the current panel
     *
     * @param newPanel the new panel to load
     */
    public void setPanel(Panelable newPanel) {
        frame.setContentPane(newPanel.getPanel());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Fetches the current user's login status and presents the appropriate page. The login and main menu pages are a
     * bit special since they are at the top level and don't have a "parent" panel themselves.
     * <p>
     * All subsequent panels should be loaded using the setPanel method.
     */
    public void refreshLogin() {
        if (controllerBundle.getUserController().getCurrentUser() != null) {
            setPanel(new MainMenuUI(this));
        } else {
            setPanel(new LoginUI(this));
        }
    }

    /**
     * Gets the controller bundle
     *
     * @return the controller bundle
     */
    public ControllerBundle getControllerBundle() {
        return controllerBundle;
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

        refreshLogin();
    }
}
