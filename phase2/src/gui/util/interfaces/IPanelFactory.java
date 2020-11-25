package gui.util.interfaces;

import gui.util.enums.Names;

import java.util.Map;

public interface IPanelFactory {
    IPanel createPanel(Names.panelNames name);

    IPanel createPanel(Names.panelNames name, Map<String, Object> arguments);
}
