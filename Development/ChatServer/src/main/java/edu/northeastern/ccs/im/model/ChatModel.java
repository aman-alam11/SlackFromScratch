package edu.northeastern.ccs.im.model;

import java.util.Date;

import edu.northeastern.ccs.im.database.User;

/**
 * Structure for ChatModel:
 *
 * { from: to: conversation: time: isDelivered: }
 */
public class ChatModel {

  private User from;
  private User to;
  private String conversation;
  private Date time;
  private boolean isDelivered;

  public ChatModel(User from, User to, String conversation, Date time, boolean isDelivered) {

    this.from = from;
    this.to = to;
    this.conversation = conversation;
    this.time = time;
    this.isDelivered = isDelivered;
  }


  public User getSender() {
    return from;
  }

  public User getReciever() {
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
