package gui.util.enums;

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
        ROOM_FORM
    }

    public enum dialogType {
        ERROR,
        INFORMATION,
        WARNING,
        QUESTION,
        PLAIN
    }

    public enum optionType {
        DEFAULT_OPTION,
        YES_NO_OPTION,
        YES_NO_CANCEL_OPTION,
        OK_CANCEL_OPTION
    }
}
