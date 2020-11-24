package gui.util.interfaces;

import gui.util.enums.PanelNames;

import java.util.Map;

public interface IPanelFactory {
    IPanel createPanel(PanelNames.names name);

    IPanel createPanel(PanelNames.names name, Map<String, Object> arguments);
}
