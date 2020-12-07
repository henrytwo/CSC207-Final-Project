package gui.conference.events.details;

import convention.exception.FullEventException;
import gui.conference.tabs.ConferenceTabsConstants;
import gui.conference.util.AbstractConferencePresenter;
import gui.util.enums.DialogFactoryOptions;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IDialog;
import gui.util.interfaces.IFrame;

import java.util.*;

class EventsDetailsPresenter extends AbstractConferencePresenter {

    private IEventsDetailsView eventsGeneralView;
    private IFrame mainFrame;

    private UUID eventUUID;

    private Map<String, Object> initializationArguments;

    EventsDetailsPresenter(IFrame mainFrame, IEventsDetailsView eventGeneralView, UUID defaultEventUUID, UUID conferenceUUID, Map<String, Object> initializationArguments) {
        super(mainFrame, conferenceUUID);

        this.mainFrame = mainFrame;
        this.eventsGeneralView = eventGeneralView;
        this.initializationArguments = initializationArguments;

        this.eventUUID = defaultEventUUID;

        updateUserData();
        updateGeneralData();
        updateButtons();
    }

    private void updateUserData() {
        if (conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID)) {
            updateAttendeeTable();
        }

        updateSpeakerTable();
    }

    private void updateButtons() {
        boolean isRegistered = eventController.isRegistered(conferenceUUID, signedInUserUUID, eventUUID);
        boolean isOrganizer = conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID);

        // Speaker for THIS event
        boolean isSpeaker = eventController.getEventSpeakers(conferenceUUID, signedInUserUUID, eventUUID).contains(signedInUserUUID);

        if (isRegistered) {
            eventsGeneralView.setRegisterButtonText("Unregister");
        }

        if (!isOrganizer) {
            eventsGeneralView.enableEditEventButton(false);
            eventsGeneralView.enableDeleteEventButton(false);
            eventsGeneralView.enableMessageUserButton(false);
        }

        if (!isRegistered && !isOrganizer && !isSpeaker) {
            eventsGeneralView.enableEventConversationButton(false);
        }

        if (isOrganizer || isSpeaker) {
            eventsGeneralView.enableRegisterButton(false);
        }
    }

    void toggleRegistration() {
        if (eventController.isRegistered(conferenceUUID, signedInUserUUID, eventUUID)) {
            IDialog confirmLeaveDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                {
                    put("message", String.format("Are you sure you want to unregister from this event? (%s)", eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUID)));
                    put("title", "Confirm unregister Event");
                    put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                    put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                }
            });

            if ((boolean) confirmLeaveDialog.run()) {
                eventController.unregisterForEvent(conferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);
                reloadEventsPage(eventUUID);
            }
        } else {
            try {
                eventController.registerForEvent(conferenceUUID, signedInUserUUID, signedInUserUUID, eventUUID);

                IDialog registeredDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                    {
                        put("message", "You have successfully registered for this event.");
                        put("title", "Registered");
                        put("messageType", DialogFactoryOptions.dialogType.INFORMATION);
                    }
                });

                registeredDialog.run();

                reloadEventsPage(eventUUID);
            } catch (FullEventException e) {
                IDialog fullCapacityDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                    {
                        put("message", "Sorry, that event is already at full capacity.");
                        put("title", "Error");
                        put("messageType", DialogFactoryOptions.dialogType.ERROR);
                    }
                });

                fullCapacityDialog.run();
            }
        }
    }

    void deleteEvent() {
        IDialog confirmDeleteDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
            {
                put("message", String.format("Are you sure you want to delete this event? (%s)", eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUID)));
                put("title", "Confirm delete Event");
                put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
            }
        });

        if ((boolean) confirmDeleteDialog.run()) {
            eventController.deleteEvent(conferenceUUID, signedInUserUUID, eventUUID);
            reloadEventsPage(null);
        }
    }

    void messageUser() {
        IDialog chooseUsersDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MULTI_USER_PICKER, new HashMap<String, Object>() {
            {
                put("instructions", "Select users to add to the new conversation");
                put("availableUserUUIDs", new HashSet<UUID>() {
                    {
                        addAll(conferenceController.getOrganizers(conferenceUUID, signedInUserUUID));
                        addAll(eventController.getEventAttendees(conferenceUUID, signedInUserUUID, eventUUID));
                        addAll(eventController.getEventSpeakers(conferenceUUID, signedInUserUUID, eventUUID));
                        add(signedInUserUUID);
                    }
                });
            }
        });

        Set<UUID> selectedUserUUIDs = (Set<UUID>) chooseUsersDialog.run();

        if (selectedUserUUIDs != null) {
            selectedUserUUIDs.add(signedInUserUUID); // We need to add the signed in user in the conversation too

            UUID conversationUUID = conferenceController.createConversationWithUsers(conferenceUUID, signedInUserUUID, selectedUserUUIDs);

            IDialog conversationCreatedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                {
                    put("messageType", DialogFactoryOptions.dialogType.INFORMATION);
                    put("message", String.format("Conversation with %d users created successfully (%s)", selectedUserUUIDs.size(), conversationUUID));
                    put("title", "Conversation created");
                }
            });

            conversationCreatedDialog.run();

            // Open the new conversation
            openMessage(conversationUUID);
        }
    }

    void eventConversation() {
        UUID eventConversationUUID = eventController.getEventConversationUUID(conferenceUUID, signedInUserUUID, eventUUID);
        String eventName = eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUID);

        boolean canCreateConversation = eventController.getEventSpeakers(conferenceUUID, signedInUserUUID, eventUUID).contains(signedInUserUUID) ||
                conferenceController.isOrganizer(conferenceUUID, signedInUserUUID, signedInUserUUID);

        if (eventConversationUUID == null) {

            // Anyone can open the event conversation, but only speakers can create it
            if (canCreateConversation) {
                IDialog confirmEventConversation = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.CONFIRM_BOOLEAN, new HashMap<String, Object>() {
                    {
                        put("message", String.format("There is currently no conversation associated with this event. Do you want to create an event conversation for %s? All users associated with this event will be added to the conversation.", eventName));
                        put("title", "Create event conversation");
                        put("messageType", DialogFactoryOptions.dialogType.QUESTION);
                        put("confirmationType", DialogFactoryOptions.optionType.YES_NO_OPTION);
                    }
                });

                if ((boolean) confirmEventConversation.run()) {
                    UUID newEventConversationUUID = eventController.createEventConversation(conferenceUUID, signedInUserUUID, eventUUID);

                    IDialog conversationCreatedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                        {
                            put("message", String.format("Successfully created an event conversation for %s", eventName));
                            put("title", "Conversation created");
                            put("messageType", DialogFactoryOptions.dialogType.INFORMATION);
                        }
                    });

                    conversationCreatedDialog.run();
                    openMessage(newEventConversationUUID);
                }
            } else {

                IDialog conversationCreatedDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.MESSAGE, new HashMap<String, Object>() {
                    {
                        put("message", "This event currently doesn't have a conversation associated with it. Contact a speaker or organizer for further assistance.");
                        put("title", "Access denied");
                        put("messageType", DialogFactoryOptions.dialogType.ERROR);
                    }
                });

                conversationCreatedDialog.run();
            }

        } else {
            openMessage(eventConversationUUID);
        }
    }

    private void openMessage(UUID conversationUUID) {
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU, new HashMap<String, Object>() {
            {
                put("defaultConversationUUID", conversationUUID);
                put("defaultTabName", ConferenceTabsConstants.tabNames.ALL_EVENTS);
                put("defaultTabIndex", 1);
            }
        }));
    }

    void editEvent() {
        IDialog eventFormDialog = dialogFactory.createDialog(DialogFactoryOptions.dialogNames.EVENT_FORM, new HashMap<String, Object>() {
            {
                put("conferenceUUID", conferenceUUID);
                put("eventUUID", eventUUID);
            }
        });

        if (eventFormDialog.run() != null) {
            // Reload the main menu to update changes
            reloadEventsPage(eventUUID);
        }
    }

    private void reloadEventsPage(UUID defaultEventUUID) {
        mainFrame.setPanel(panelFactory.createPanel(PanelFactoryOptions.panelNames.MAIN_MENU, new HashMap<String, Object>(initializationArguments) {
            {
                put("defaultConferenceUUID", conferenceUUID);
                put("defaultTabName", ConferenceTabsConstants.tabNames.ALL_EVENTS);
                put("defaultEventUUID", defaultEventUUID);
                put("defaultTabIndex", 0);
            }
        }));
    }

    private void updateGeneralData() {
        String[][] tableData = {
                {"Event Name", eventController.getEventTitle(conferenceUUID, signedInUserUUID, eventUUID)},
                {"Start", eventController.getEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID).getStart().toString()},
                {"End", eventController.getEventTimeRange(conferenceUUID, signedInUserUUID, eventUUID).getEnd().toString()},
                {"UUID", eventUUID.toString()},
                {"Conversation UUID", eventController.getEventConversationUUID(conferenceUUID, signedInUserUUID, eventUUID) == null ? "N/A" : eventController.getEventConversationUUID(conferenceUUID, signedInUserUUID, eventUUID).toString()},
                {},
                {"Room Location", "" + roomController.getRoomLocation(conferenceUUID, signedInUserUUID, eventController.getEventRoom(conferenceUUID, signedInUserUUID, eventUUID))},
                {},
                {"# Attendees", "" + eventController.getNumRegistered(conferenceUUID, signedInUserUUID, eventUUID) + "/" + roomController.getRoomCapacity(conferenceUUID, signedInUserUUID, eventController.getEventRoom(conferenceUUID, signedInUserUUID, eventUUID))},
                {"# Speakers", "" + eventController.getEventSpeakers(conferenceUUID, signedInUserUUID, eventUUID).size()},
        };

        String[] columnNames = {
                "Key",
                "Value"
        };

        eventsGeneralView.setGeneralTableData(tableData, columnNames);
    }

    private String[][] generateUserTable(Set<UUID> userUUIDs) {
        String[][] names = new String[userUUIDs.size()][1];

        int index = 0;

        for (UUID userUUID : userUUIDs) {
            names[index][0] = userController.getUserFullName(userUUID);

            index++;
        }

        return names;
    }

    private void updateAttendeeTable() {
        Set<UUID> attendeeUUIDs = eventController.getEventAttendees(conferenceUUID, signedInUserUUID, eventUUID);

        String[][] tableData = generateUserTable(attendeeUUIDs);

        String[] columnNames = {
                "Event Attendees",
        };

        eventsGeneralView.setAttendeeTableData(tableData, columnNames);
    }

    private void updateSpeakerTable() {
        Set<UUID> speakerUUIDs = eventController.getEventSpeakers(conferenceUUID, signedInUserUUID, eventUUID);

        String[][] tableData = generateUserTable(speakerUUIDs);

        String[] columnNames = {
                "Event Speakers",
        };

        eventsGeneralView.setSpeakerTableData(tableData, columnNames);
    }
}