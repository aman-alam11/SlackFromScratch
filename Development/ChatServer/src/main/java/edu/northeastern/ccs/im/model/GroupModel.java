package edu.northeastern.ccs.im.model;

import java.util.Date;
import java.util.List;

public class GroupModel {

  private List<String> groupUsers;
  private List<String> moderators;
  private Date creationTime;
  private String groupCreator;


  public Date getCreationTime() {
    return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  public List<String> getModerators() {
    return moderators;
  }

  public void setModerators(List<String> moderators) {
    this.moderators = moderators;
  }

  public List<String> getGroupUsers() {
    return groupUsers;
  }

  public void setGroupUsers(List<String> groupUsers) {
    this.groupUsers = groupUsers;
  }

  public String getGroupCreator() {
    return groupCreator;
  }

  public void setGroupCreator(String groupCreator) {
    this.groupCreator = groupCreator;
  }
}
