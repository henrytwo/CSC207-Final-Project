package gui.util.interfaces;

import gui.util.interfaces.IPanel;

import java.util.Map;

public interface IPanelFactory {
    public IPanel createPanel(String name);
    public IPanel createPanel(String name, Map<String, String> arguments);
}
