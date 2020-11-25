package gui.util.factories;

import gui.conference.picker.ConferencePickerView;
import gui.util.enums.Names;
import gui.util.exception.NullDialogException;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IDialogFactory;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanel;

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
                return new ConferencePickerView(mainFrame, (Set<UUID>) arguments.get("availableConferenceUUIDs"), (String) arguments.get("instructions"));
            default:
                throw new NullDialogException(name);
        }
    }
}
