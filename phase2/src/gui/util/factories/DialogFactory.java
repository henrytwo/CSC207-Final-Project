package gui.util.factories;

import gui.conference.form.ConferenceFormDialog;
import gui.conference.picker.ConferencePickerDialog;
import gui.conference.rooms.form.RoomFormDialog;
import gui.conference.rooms.picker.RoomPickerDialog;
import gui.messaging.form.ConversationFormDialog;
import gui.user.multipicker.MultiUserPickerDialog;
import gui.user.picker.UserPickerDialog;
import gui.util.dialogs.ConfirmBooleanDialogView;
import gui.util.dialogs.MessageDialogView;
import gui.util.enums.DialogFactoryOptions;
import gui.util.exception.NullDialogException;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DialogFactory implements IDialogFactory {
    private IFrame mainFrame;

    /**
     * @param mainFrame main IFrame for the system
     */
    public DialogFactory(IFrame mainFrame) {
        this.mainFrame = mainFrame;
    }


    /**
     * Generate a dialog given its name with no parameters
     *
     * @param name
     * @return
     */
    @Override
    public IDialog createDialog(DialogFactoryOptions.dialogNames name) {
        return createDialog(name, new HashMap<>());
    }

    /**
     * Generates an IDialog given its name and (optional) initializing arguments
     *
     * @param name
     * @param arguments
     * @return
     */
    @Override
    public IDialog createDialog(DialogFactoryOptions.dialogNames name, Map<String, Object> arguments) {
        switch (name) {
            case CONVERSATION_FORM:
                return new ConversationFormDialog(mainFrame, (UUID) arguments.get("conversationUUID"));
            case CONFERENCE_FORM:
                return new ConferenceFormDialog(mainFrame, (UUID) arguments.get("conferenceUUID"));
            case ROOM_PICKER:
                return new RoomPickerDialog(mainFrame, (UUID) arguments.get("conferenceUUID"), (Set<UUID>) arguments.get("availableRoomUUIDs"), (String) arguments.get("instructions"));
            case CONFERENCE_PICKER:
                return new ConferencePickerDialog(mainFrame, (Set<UUID>) arguments.get("availableConferenceUUIDs"), (String) arguments.get("instructions"));
            case MULTI_USER_PICKER:
                return new MultiUserPickerDialog(mainFrame, (Set<UUID>) arguments.get("availableUserUUIDs"), (Set<UUID>) arguments.get("selectedUserUUIDs"), (String) arguments.get("instructions"));
            case USER_PICKER:
                return new UserPickerDialog(mainFrame, (Set<UUID>) arguments.get("availableUserUUIDs"), (String) arguments.get("instructions"));
            case MESSAGE:
                return new MessageDialogView(mainFrame, (String) arguments.get("message"), (String) arguments.getOrDefault("title", "Message"), (DialogFactoryOptions.dialogType) arguments.get("messageType"));
            case ROOM_FORM:
                return new RoomFormDialog(mainFrame, (UUID) arguments.get("conferenceUUID"), (UUID) arguments.get("roomUUID"));
            case CONFIRM_BOOLEAN:
                return new ConfirmBooleanDialogView(mainFrame, (String) arguments.get("message"), (String) arguments.getOrDefault("title", "Confirm"), (DialogFactoryOptions.dialogType) arguments.get("messageType"), (DialogFactoryOptions.optionType) arguments.get("confirmationType"));
            default:
                throw new NullDialogException(name);
        }
    }
}
