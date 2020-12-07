package gui.user.multipicker;

import gui.util.AbstractPresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.*;

class MultiUserPickerPresenter extends AbstractPresenter {

    private IMultiUserPickerDialog multiUserPickerDialog;
    private Set<UUID> availableUserUUIDs;
    private Set<UUID> selectedUserUUIDs = new HashSet<>();

    MultiUserPickerPresenter(IFrame mainFrame, IMultiUserPickerDialog multiUserPickerDialog, Set<UUID> availableUserUUIDs, Set<UUID> selectedUserUUIDs) {
        super(mainFrame);

        this.multiUserPickerDialog = multiUserPickerDialog;
        this.availableUserUUIDs = availableUserUUIDs;

        if (selectedUserUUIDs != null) {
            this.selectedUserUUIDs = selectedUserUUIDs;
        }

        updateUserList();
    }

    private void updateUserList() {
        List<UUID> orderedUserUUIDs = new ArrayList<>(selectedUserUUIDs);
        String[] userNames = new String[orderedUserUUIDs.size()];

        for (int i = 0; i < orderedUserUUIDs.size(); i++) {
            userNames[i] = userController.getUserFullName(orderedUserUUIDs.get(i));
        }

        multiUserPickerDialog.setUserList(userNames);

        // Disable buttons if they're not relevant
        multiUserPickerDialog.setRemoveButtonDisabled(selectedUserUUIDs.size() == 0);
        multiUserPickerDialog.setAddButtonDisabled(new HashSet<UUID>(availableUserUUIDs) {
            {
                removeAll(selectedUserUUIDs);
            }
        }.size() == 0);
    }

    void addUser() {
        IDialog addUserDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Add user to the list");
                put("availableUserUUIDs", new HashSet<UUID>(availableUserUUIDs) {
                    {
                        removeAll(selectedUserUUIDs); // You should only be able to add users who aren't already on the list
                    }
                });
            }
        });

        UUID newUserUUID = (UUID) addUserDialog.run();

        if (newUserUUID != null) {
            selectedUserUUIDs.add(newUserUUID);
            updateUserList();
        }
    }

    void removeUser() {
        IDialog removeUserDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Remove user from the list");
                put("availableUserUUIDs", new HashSet<>(selectedUserUUIDs));
            }
        });

        UUID selectedUserUUID = (UUID) removeUserDialog.run();

        if (selectedUserUUID != null) {
            selectedUserUUIDs.remove(selectedUserUUID);
            updateUserList();
        }
    }

    void submit() {
        multiUserPickerDialog.setSelectedUserUUIDs(selectedUserUUIDs);
        multiUserPickerDialog.close();
    }
}
