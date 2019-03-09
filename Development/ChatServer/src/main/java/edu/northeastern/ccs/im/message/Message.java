package edu.northeastern.ccs.im.message;

import java.util.Date;

public interface Message {
	MessageType getMessageType();
	String getFromUser();
	String getSendToUser();
	Date getCreationTime();
	String getMessage();

}
