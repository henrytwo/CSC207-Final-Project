package gui.conference.rooms.form;

import java.util.UUID;

public interface IRoomFormDialog {
    void setRoomUUID(UUID newUUID);

    void setDialogTitle(String title);

    void setLocation(String roomLocation);

    void setCapacity(int roomCapacity);

    void close();

    void setUpdated(boolean updated);

    String getRoomLocation();

    int getCapacity();

}
