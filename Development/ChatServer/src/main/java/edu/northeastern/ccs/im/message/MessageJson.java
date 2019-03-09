package edu.northeastern.ccs.im.message;

import java.util.Date;

public class MessageJson implements Message {
	private MessageType messageType;
	private String fromUser;
	private String sendToUser;
	private Date creationTime;
	private String message;
	
	public MessageJson(String senderName, MessageType type, String message) {
		this.fromUser = senderName;
		this.messageType = type;
		this.message = message;
		this.creationTime = new Date();
	}
	
	public MessageJson(MessageType type) {
		this.messageType = type;
	}
	
	public MessageType getMessageType() {
		return messageType;
	}
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getSendToUser() {
		return sendToUser;
	}
	public void setSendToUser(String sendToUser) {
		this.sendToUser = sendToUser;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
