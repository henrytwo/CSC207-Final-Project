package gui.util.interfaces;

import gui.util.enums.PanelFactoryOptions;

import java.util.Map;

public interface IPanelFactory {
    IPanel createPanel(PanelFactoryOptions.panelNames name);

    IPanel createPanel(PanelFactoryOptions.panelNames name, Map<String, Object> arguments);
}
