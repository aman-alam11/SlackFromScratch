package edu.northeastern.ccs.im.clientmenu.models;

import java.util.Date;

public class UserChat {

  private String fromUserName;
  private String toUserName;
  private String msg;
  private Date timestamp;
  private Date expiry;
  private Boolean isDelivered;
  private String errorCode;

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
}
