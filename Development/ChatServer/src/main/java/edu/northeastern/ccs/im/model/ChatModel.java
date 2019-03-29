package edu.northeastern.ccs.im.model;

import java.util.Date;

public class ChatModel {

  private String fromUserName;
  private String toUserName;
  private String msg;
  private Date timestamp;
  private Date expiry;
  private Boolean isDelivered;
  private String errorCode;
  private String groupName;
  private String replyTo;

  public String getErrorCode() {
    return errorCode;
  }

  public Boolean getDelivered() {
    return isDelivered;
  }

  public void setDelivered(Boolean delivered) {
    isDelivered = delivered;
  }

  public Date getExpiry() {
    return expiry;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getToUserName() {
    return toUserName;
  }

  public void setToUserName(String toUserName) {
    this.toUserName = toUserName;
  }

  public String getFromUserName() {
    return fromUserName;
  }

  public void setFromUserName(String fromUserName) {
    this.fromUserName = fromUserName;
  }
  
	public String getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
  
}
