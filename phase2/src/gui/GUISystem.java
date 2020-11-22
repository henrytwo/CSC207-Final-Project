package gui;

import util.ControllerBundle;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUISystem {
    ControllerBundle controllerBundle;

    Runnable shutdown;
    MainMenu mainMenu;

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
     * If the user is not logged in, present login/register prompts. Otherwise, send them to the main menu.
     */
    public void run() {

        // this is some serious testing stuff... so I'm not sure if this is how it's supposed to work
        // also, this stuff seems to run on a different thread, so we gotta fix how saving to disk is done
        this.mainMenu = new MainMenu(controllerBundle);

        JFrame frame = new JFrame("Bad LinkedIn Clone");
        frame.setContentPane(mainMenu.getPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Adds listener to run shutdown sequence
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                shutdown.run();
            }
        });
    }
}
