package gui.user.multipicker;

import java.util.Set;
import java.util.UUID;

public interface IMultiUserPickerDialog {
    void close();
    void setDialogTitle(String newTitle);
    void setUserList(String[] userList);
    void setInstructions(String instructions);
    void setAddButtonDisabled(boolean state);
    void setRemoveButtonDisabled(boolean state);
    void setSelectedUserUUIDs(Set<UUID> selectedUserUUIDs);
}
