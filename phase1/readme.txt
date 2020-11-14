CSC207 Group 11 - Phase 1

Note: Data is serialized and saved when the program is exited gracefully. In other words, please exit the program by
      going to the main menu and selecting "Exit System". Abruptly terminating the program may result in lost data.

The first menu will be a login/register prompt if no user is currently logged in. Registering account will automatically
log you into that account. Assuming the aforementioned shutdown procedure is followed, you will remained logged in the
next time the program is started. You may also logout to clear this.

This program is split into two sections:
1. Messaging/Contacts
2. Conferences


+ Messaging/Contacts

The Messaging/Contacts portion of the system works independently of the Conferences part, but the Conference system has
the authority to create conversations bypassing any restrictions imposed by the Messaging system (e.g. there is a rule
that users can only message people on their friends list).

Definitions:
"Conversation" - A collection of messages and metadata about which users have read/write access to these messages (think
                 of it like a group chat with any number of users -- a groupchat of size 2 is essentially a direct msg).

"Message" - A single contribute to the conversation by a user. It contains additional metadata, such as the timestamp
            and the sender UUID, which is why it is its own object.

Users may send contact requests to other people and view who has requested to contact them. A user may initiate a
conversation with another party if the other person is on the contact list of the sender.

Conversations are designed with read/write access in mind so that it would be possible to expand in the future and add
functionality such as announcement channels, etc.

Speaker and Organizers are able to message users in their respective conferences through the Conference System. This
will be addressed in the section part

Attendees may add speakers or other attendees as friends and message them that way.


+ Conferences

A conference is defined as a collection of users, events, and rooms at a scheduled time. We have ignored the assumptions
made about the talk length and when they will take place and have instead allowed the user to choose this themselves.

A user may either create a new conference (making them the first organizer of that conferences), join an existing
existing conference as an attendee, or view conferences they've already joined.

After selecting a conference, the user may view a full list of events, which includes a list of all the events
and their respective times. Attendees may register in these events if they aren't full.

Attendees may view a list of events they are registered in and amend their registration status if needed.

Similarly, speakers may view a list of events which they have been assigned as a speaker. They may also create a
conversation for a particular event, which will automatically add all the users who are registered for the event.
Any users who join the event later will be automatically added as well. If an attendee unregisters, they will no longer
be allowed to access the conversation.

Organizers essentially have the ultimate authority when it comes to conferences. They have the permission to do
anything that the other roles are able to do. Two of the most important operations are creating Events and Rooms.

Each Event is linked to a Room, which keeps track of bookings (to avoid conflicts) and contains metadata including
the capacity and location. When an event is created, the system will consult the Room to check for conflicts, and
also checks all the other Events that each Speaker is part of to ensure there are no booking conflicts there either.
If there is a conflict, the system will refuse to execute the operation.

Organizers are able to set a room capacity, which will be enforced when a user attempts to register for an event.

The controllers have been built to enable organizers to edit metadata and even delete Rooms, Events, etc; however,
those features are not enabled in this build as they aren't part of the requirements.

+ UI

The UI for this phase consists of command line inputs, as per the requirements. However, the controllers have been
structured in a way that should make it easy to make it work with a GUI or other interface. See the ConferenceSystem
class for more details.