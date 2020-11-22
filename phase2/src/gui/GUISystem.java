package gui;

import util.ControllerBundle;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

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

        // Lambda function to allow child frames to "request" a frame
        Consumer<Panelable> setPanel = (newPanel) -> {
            System.out.println("Setting panel: " + newPanel);

            frame.setContentPane(newPanel.getPanel());

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        };

        /**
         * Currently, auto login doesn't work since the set panel here is after the constructor. Will be fixed eventually.
         * -Henry
         */
        Panelable loginUI = new LoginUI(controllerBundle, setPanel);
        setPanel.accept(loginUI);
    }
}
