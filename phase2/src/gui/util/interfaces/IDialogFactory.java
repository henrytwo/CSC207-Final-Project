package gui.util.interfaces;

import gui.util.enums.DialogFactoryOptions;

import java.util.Map;

/**
 * Dialog factory interface
 */
public interface IDialogFactory {
    IDialog createDialog(DialogFactoryOptions.dialogNames name);

    IDialog createDialog(DialogFactoryOptions.dialogNames name, Map<String, Object> arguments);
}
