CSC207 Group 11 - Phase 2

Important: Ensure that the working directory for the project is /phase2 to ensure all the resources are read correctly

Note: due to some silly problems with some team member's systems, Main.java has been changed to MainSystem.java.
      MainSystem.java is now the main entry point to the program

+ Mandatory Extensions
    - Different event types (Already implemented in phase 1)
        - Events may have any number of speakers (Just add/remove speakers from the event)
    - Cancel event (Already implemented in phase 1)
        - Events may be cancelled by an organizer by deleting it
    - Additional user type
        - See god mode section below
    - Organizers create other accounts (Already implemented in phase 1)
        - Attendees can be invited under the Conference Settings tab
        - Organizers can be invited under the Conference Settings tab
        - Speakers can be invited by assigning them to an event
        - Gods can only be added by modifying godUsers.csv for security reasons
    - Maximum attendees per event
        - The limit for events are set at the room level
        - Amendments are made under Manage Rooms
        - Note: Reducing room capacity below the current # of enrollments does not automatically remove users.
                it is the responsibility of the organizer to handle this situation manually

+ Optional Extensions
    - Allow user to select conference (Already implemented in phase 1)
        - Use the left sidebar on the main menu to navigate between conferences
    - Allow users to mark messages as read, delete, archive, etc.
        - Implemented under the Messaging tab
        - Note that God users cannot archive conversations since they always have access to everything
    - Allow the user to produce a formatted schedule that can be downloaded
        - Implemented under the Schedule Download tab
    - Implemented GUI
        - Run the program to see it
    - Statistics
        - On the general tab of Conferences there are a list of statistics about the conference
        - Similarly, after selecting an Event there is a list of statistics

+ Custom Features
    - Contacts system
        - Restricts who a user can message to people on their contact list

+ Using god mode
    To modify the list of god mode users, modify godUser.csv (found in the root directory). The columns of this CSV file
    are as followed:

    First Name, Last Name, Username, Password

    The column delimiter is a comma. Note: whitespace before and after commas will be treated as literals. (i.e. not
    stripped) The system will check this list and automatically register users that haven't already been registered
    on startup.

    Here's an account with god-mode permissions:
    Username: god
    Password: god

    Note: In god mode, the act of leaving an event does nothing to the membership status, since membership for gods
          is determined beyond the scope of a conference.

+ Scripts
    There is a package called scripts which contains tools that allow for many conferences, events, users, and rooms to
    be generated for testing purposes. This script is extremely sketchy and should not be marked.

    If you want to use this script for testing, please close the program first before running the script. After the
    script is executed, the program can be started again. A god user can be used to view all the conferences for easy
    testing.

+ Design Patterns
    - Dependency Injection
        - Classes
            ConferenceController
            EventController
            RoomController
            ScheduleController

        Dependency injection was used to inject the Managers into these Controllers, which increases flexibility and
        allows us to do things such as serialize managers, and share managers between controllers.

    - Factory
        - Classes
            PanelFactory
            DialogFactory

        The factory design pattern was used to generate Panels and Dialogs in the GUI to comply with the MVP
        architecture. Since views are at a higher level than presenters, a factory was used to create View objects
        through an interface.

    - Parameter Object
        - Classes
            ControllerBundle - Used to avoid repeating the same parameter list of controllers throughout the UI.
            TimeRange - in the system we often have start and end dates, so it makes sense to combine them into a single
                        object.
