package gui.util.enums;

/**
 * Enums pertaining to the dialog factory
 */
public class DialogFactoryOptions {
    public enum dialogNames {
        USER_PICKER,
        MULTI_USER_PICKER,
        ROOM_PICKER,
        CONFERENCE_PICKER,
        CONFERENCE_FORM,
        MESSAGE,
        CONFIRM_BOOLEAN,
        CONVERSATION_FORM,
        ROOM_FORM,
        EVENT_FORM
    }

    /**
     * Enums of dialogue types
     */
    public enum dialogType {
        ERROR,
        INFORMATION,
        WARNING,
        QUESTION,
        PLAIN
    }

    /**
     * Enums of option types
     */
    public enum optionType {
        DEFAULT_OPTION,
        YES_NO_OPTION,
        YES_NO_CANCEL_OPTION,
        OK_CANCEL_OPTION
    }
}
