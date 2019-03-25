package edu.northeastern.ccs.im.model;

import java.util.Date;
import java.util.List;

public class GroupModel {

	private String groupName;
	private List<String> moderators;
  private Date creationTime;
  private String groupCreator;
  //whether moderator approval required to add members
  private boolean isAuthRequired;

  public String getGroupName() {
  	return groupName;
  }
  
  public void setGroupName(String groupName) {
  	this.groupName = groupName;
  }
  
  public boolean isAuthRequired() {
		return isAuthRequired;
	}

	public void setAuthRequired(boolean isAuthRequired) {
		this.isAuthRequired = isAuthRequired;
	}

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

  public String getGroupCreator() {
    return groupCreator;
  }

  public void setGroupCreator(String groupCreator) {
    this.groupCreator = groupCreator;
  }
}
