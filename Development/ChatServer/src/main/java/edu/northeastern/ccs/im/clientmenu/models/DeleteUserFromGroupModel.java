package edu.northeastern.ccs.im.clientmenu.models;

import edu.northeastern.ccs.im.message.MessageType;

import java.util.Date;

public class DeleteUserFromGroupModel {

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    private MessageType messageType;
    private String fromUser;

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;
    private Date creationTime;

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

}
