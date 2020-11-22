package gui.util.factories;

import gui.mainMenu.conference.ConferenceMenuView;
import gui.mainMenu.conference.TestView;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;
import gui.util.interfaces.IPanelFactory;

import java.util.Map;

public class PanelFactory implements IPanelFactory {
    IPanel parent;
    IFrame mainFrame;

    /**
     * @param mainFrame main IFrame for the system
     * @param parent    reference to the parent panel (the one before the new frame)
     */
    public PanelFactory(IFrame mainFrame, IPanel parent) {
        this.parent = parent;
        this.mainFrame = mainFrame;
    }

    @Override
    public IPanel createPanel(String name) {
        return createPanel(name, null);
    }

    @Override
    public IPanel createPanel(String name, Map<String, String> arguments) {
        switch (name) {
            case "Test":
                return new TestView(mainFrame, parent, arguments.get("conference"));
            case "Conference":
                return new ConferenceMenuView(mainFrame, parent);
            default:
                return null;
        }
    }
}
