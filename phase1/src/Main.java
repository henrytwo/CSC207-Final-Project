public class Main {


    /*
     * User flow
     *
     * ** Make attendee operations executable by admins too
     * ** So it'd be check if organizer OR (if target == self AND is attendee)
     *
     * /
     * |-Login or Register                                           ** Ez clap menu thing (Shubhra)
     *   |- Go to messaging
     *   |  |-Look at the list of messages                           ** Ez clap menu thing
     *   |  | |-Select a conversation                                ** Custom message boi (Mahak)
     *   |  |   |-Send message
     *   |  |   |-Read message
     *   |  |   |-See users in conversation
     *   |  |-Start a new message with someone on your contact list
     *   |
     *   |- Go to contacts                                            ** Ez clap menu thing (Pranjal)
     *   |  |-View contacts                                           ** Custom stuff
     *   |  |-Request someone to connect
     *   |  |-View people who want to slide into your DMs
     *   |
     *   |- Go to conferences                                         ** Ez clap menu thing
     *  |-> Create a conference                                   ** Form boi
     *  |-> Join a conference                                     ** Ez clap menu thing
     *  |   |-> Find a conference from a list and join it
     *  |-> View a joined conference                              ** Ez clap menu thing (Ellie/Henry/Shubhra)
     *      |-> View the general conference details (start, end, name, etc.)
     *      |
     *      |-> View the event calendar
     *      |
     *      |-> Event stuff (Emre)
     *      |   |-> View list of events (Attendee)
     *      |   |-> View list of events (Speaker)
     *      |   |-> View event room
     *      |   |-> View event time stuff
     *      |   |-> Register for event
     *      |   |-> Unregister from event
     *      |   |-> Make a conversation for everyone in this event (Speaker)
     *      |   |-> View a list of attendees, speakers
     *      |   |-> Organizer related operations
     *      |       |-> Edit the name, dates, etc.
     *      |       |-> Create event
     *      |       |-> Delete event
     *      |
     *      |-> Room stuff (Antara)
     *      |   |-> View calendar
     *      |   |-> Organizer related operations
     *      |        |-> Edit the room capacity, location, etc.
     *      |        |-> Create room
     *      |        |-> Delete room
     *      |
     *      |-> Conference management stuff
     *          |-> Delete the conference
     *          |-> Slide into the DM of any attendee
     *          |-> Add/Remove organizer
     * */

    /**
     * UI Components to build
     * - n-column table with numbered rows (Shubhra)
     * - messaging thing (Mahak)
     * - Form component (Henry)
     * - Calendar (Ellie)
     */

    /**
     * TODO: Don't pass around Message objects at the top level
     */

    public static void main(String[] args) {
        ConventionSystem cs = new ConventionSystem();
        cs.run();
    }
}
