package edu.northeastern.ccs.im.model;

import java.util.Date;

public class UnreadMessageModel {

  private String fromPersonName;
  private Date timeStamp;
  private String message;
  private boolean isGroupMessage;

  public UnreadMessageModel(String fromPersonName, String message, Date timeStamp, boolean isGroupMessage) {
    this.fromPersonName = fromPersonName;
    this.timeStamp = timeStamp;
    this.message = message;
    this.isGroupMessage = isGroupMessage;
  }

  public boolean isGroupMessage() {
    return isGroupMessage;
  }

  @Override
  public String toString() {
    return "FROM: " + fromPersonName + " TIME: " + timeStamp + " MESSAGE: " + message;
  }
}
