package edu.northeastern.ccs.im.clientmenu.userlevel;

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
  }

  public SuperUserMessageModel(boolean getAllChats, String username) {
    this.mGroupToTap = username;
    mGetOnlyUserChat = false;
    mGetOnlyGroupChat = false;
    mGetAllChats = getAllChats;
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

  public boolean isAreDatesValid() {
    return areDatesValid;
  }

  public void setAreDatesValid(boolean areDatesValid) {
    this.areDatesValid = areDatesValid;
  }
}
