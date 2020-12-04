CSC207 Group 11 - Phase 2

Important: Ensure that the working directory for the project is /phase2 to ensure all the resources are read correctly

Note: due to some silly problems with some team member's systems, Main.java has been changed to MainSystem.java.
      MainSystem.java is now the main entrypoint to the program

+ Change log
    - God mode added (god mode users are organizers for all conferences in the system)
    - GUI added

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