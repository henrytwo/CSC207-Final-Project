package gui.util.swing;

import gui.util.enums.DialogFactoryOptions;

import javax.swing.*;

/**
 * Translates between enums and the constants that swing uses
 */
public class ConstantsTranslator {
    public int translateDialogType(DialogFactoryOptions.dialogType dialogType) {
        switch (dialogType) {
            case ERROR:
                return JOptionPane.ERROR_MESSAGE;
            case WARNING:
                return JOptionPane.WARNING_MESSAGE;
            case QUESTION:
                return JOptionPane.QUESTION_MESSAGE;
            case INFORMATION:
                return JOptionPane.INFORMATION_MESSAGE;
            default:
                return JOptionPane.PLAIN_MESSAGE;
        }
    }

    public int translateOptionType(DialogFactoryOptions.optionType optionType) {
        switch (optionType) {
            case YES_NO_OPTION:
                return JOptionPane.YES_NO_OPTION;
            case YES_NO_CANCEL_OPTION:
                return JOptionPane.YES_NO_CANCEL_OPTION;
            case OK_CANCEL_OPTION:
                return JOptionPane.OK_CANCEL_OPTION;
            default:
                return JOptionPane.DEFAULT_OPTION;
        }
    }
}
