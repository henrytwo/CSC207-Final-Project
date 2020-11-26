package gui.util.factories;

import gui.conference.picker.ConferencePickerDialog;
import gui.util.dialogs.message.MessageDialogView;
import gui.util.enums.Names;
import gui.util.exception.NullDialogException;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;

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
    public IDialog createDialog(Names.dialogNames name) {
        return createDialog(name, null);
    }

    /**
     * Generates an IDialog given its name and (optional) initializing arguments
     *
     * @param name
     * @param arguments
     * @return
     */
    @Override
    public IDialog createDialog(Names.dialogNames name, Map<String, Object> arguments) {
        switch (name) {
            case CONFERENCE_PICKER:
                return new ConferencePickerDialog(mainFrame, (Set<UUID>) arguments.get("availableConferenceUUIDs"), (String) arguments.get("instructions"));
            case MESSAGE:
                return new MessageDialogView(mainFrame, (String) arguments.get("message"), (String) arguments.get("title"), (Names.dialogType) arguments.get("messageType"));
            default:
                throw new NullDialogException(name);
        }
    }
}
