package messaging;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Contains information about individual messages, such as the sender, timestamp, etc.
 */
public class Message implements Serializable {
    private String content;
//    private Set<Message> responses = new HashSet<>();
    private LocalDateTime timestamp;
    private UUID senderId;
    private Set<UUID> hasRead = new HashSet<>();
    private Set<UUID> usersArchivingMessage = new HashSet<>();

    /**
     * Constructor for Message.
     *
     * @param messageSender_id The ID of the sender of the message
     * @param messageContent The content of the message to be sent.
     */
    public Message(UUID messageSender_id, String messageContent){
        content = messageContent;
        timestamp = LocalDateTime.now();
        senderId = messageSender_id;
    }

    /**
     *  returns the UUID of the sender of the message
     *
     * @return UUID of the message sender
     */
    public UUID getSenderId(){
        return senderId;
    }

    /**
     * Getter for the content of the message
     *
     * @return the content of the message
     */
    public String getContent() {
        return content;
    }

    /**
     * Getter for the date and time of the creation of message
     *
     * @return the date and time that the message was sent by the sender
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * The toString method for the message
     */
    @Override
    public String toString() {
        return String.format("[%s @ %s] %s\n", senderId, timestamp, content);
    }

    /**
     * Gets whether a message has been read or not
     *
     * @return the list of users who have read this message
     */
    public Set<UUID> getHasRead(){ return hasRead; }


    /**
     * adds a specific user to the set of users that have read a specific message
     *
     * @param userUUID user reading this message
     */
    public void userReadMessage(UUID userUUID){
        hasRead.add(userUUID);
    }

    /**
     * removes a specific user to the set of users that have read a specific message
     *
     * @param userUUID user unreading this message
     */
    public void userUnreadMessage(UUID userUUID){
        hasRead.remove(userUUID);
    }

    /**
     * updates the user list that has archived this message
     *
     * @param userUUID user intending to archive message
     */
    public void setUsersArchivingMessage(UUID userUUID){
        usersArchivingMessage.add(userUUID);
    }

    /**
     * gets the list of users that have archived this message
     *
     * @return the list of users that have archived this message
     */
    public Set<UUID> getUsersArchivingMessage(){
        return usersArchivingMessage;
    }

    //    public void edit_message(String new_content, Date new_timestamp){
//        content = new_content;
//        timestamp = new_timestamp;
//    }
}
