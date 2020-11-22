package gui.util.interfaces;

import java.util.Map;

public interface IPanelFactory {
    IPanel createPanel(String name);

    IPanel createPanel(String name, Map<String, String> arguments);
}
