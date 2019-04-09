package edu.northeastern.ccs.im.model;

import java.util.Date;

public class SuperUserMessageModel {

  private boolean mGetOnlyUserChat;
  private boolean mGetOnlyGroupChat;

  // For this state, simply call the unread message method on database side
  private boolean mGetAllChats;

  private Date mStartDate;
  private Date mEndDate;
  private boolean areDatesValid;

  private String mUsernameToTap;
  private String mGroupToTap;


  public SuperUserMessageModel(boolean getOnlyUserChat, boolean getOnlyGroupChat, String username) {
    this.mUsernameToTap = username;
    mGetOnlyUserChat = getOnlyUserChat;
    mGetOnlyGroupChat = getOnlyGroupChat;
    mGetAllChats = mGetOnlyGroupChat && mGetOnlyUserChat;
  }

  public SuperUserMessageModel(String username) {
    this.mGroupToTap = username;
    mGetOnlyUserChat = false;
    mGetOnlyGroupChat = false;
    mGetAllChats = false;
  }


  public Date getEndDate() {
    return mEndDate;
  }

  public Date getStartDate() {
    return mStartDate;
  }

  public boolean isGetAllChats() {
    return mGetAllChats;
  }

  public boolean isGetOnlyGroupChat() {
    return mGetOnlyGroupChat;
  }

  public boolean isGetOnlyUserChat() {
    return mGetOnlyUserChat;
  }

  public void setmStartDate(Date mStartDate) {
    this.mStartDate = mStartDate;
  }

  public void setmEndDate(Date mEndDate) {
    this.mEndDate = mEndDate;
  }

  public boolean areDatesValid() {
    return areDatesValid;
  }

  public void setAreDatesValid(boolean areDatesValid) {
    this.areDatesValid = areDatesValid;
  }

  public String getmUsernameToTap() {
    return mUsernameToTap;
  }

  public String getmGroupToTap() {
    return mGroupToTap;
  }
}
