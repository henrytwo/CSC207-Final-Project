package gui.conference.tabs;

public class ConferenceTabsConstants {
    public enum tabNames {
        GENERAL,
        ALL_EVENTS,
        YOUR_REGISTERED_EVENTS,
        YOUR_SPEAKER_EVENTS,
        ROOMS,
        SETTINGS,
    }

    public int getTabIndex(tabNames tabName) {
        switch (tabName) {
            case GENERAL:
                return 0;
            case ALL_EVENTS:
                return 1;
            case YOUR_REGISTERED_EVENTS:
                return 2;
            case YOUR_SPEAKER_EVENTS:
                return 3;
            case ROOMS:
                return 4;
            case SETTINGS:
                return 5;
            default:
                return -1;
        }
    }
}
