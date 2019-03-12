package edu.northeastern.ccs.im.model;

import java.util.Date;

import edu.northeastern.ccs.im.database.User;

/**
 * Structure for ChatModel:
 *
 * { from: to: conversation: time: isDelivered: }
 */
public class ChatModel {

  private String from;
  private String to;
  private String conversation;
  private Date time;
  private boolean isDelivered;

  public ChatModel(String from, String to, String conversation, Date time, boolean isDelivered) {

    this.from = from;
    this.to = to;
    this.conversation = conversation;
    this.time = time;
    this.isDelivered = isDelivered;
  }


  public String getSender() {
    return from;
  }

  public String getReciever() {
    return to;
  }

  public String getConversation() {
    return conversation;
  }

  public Date getTimeStamp() {
    return time;
  }

  public boolean isDelivered() {
    return isDelivered;
  }

}
