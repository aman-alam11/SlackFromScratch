package edu.northeastern.ccs.im.clientmenu.models;

import edu.northeastern.ccs.im.message.MessageType;

import java.util.Date;
import java.util.List;

public class DeleteUserFromGroupModel {

    private MessageType messageType;
    private String fromUser;
    private String message;
    private Date creationTime;

    public String getMessage() {
        return message;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}
