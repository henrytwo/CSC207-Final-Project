package gui.util.interfaces;

import gui.util.enums.Names;

import java.util.Map;

public interface IDialogFactory {
    IDialog createDialog(Names.dialogNames name);

    IDialog createDialog(Names.dialogNames name, Map<String, Object> arguments);
}
