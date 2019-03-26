package edu.northeastern.ccs.im.model;

import java.util.Date;
/**
 * This model is used to create or update group,
 * not for add/ remove users
 * @author kumar
 *
 */
public class GroupCreateUpdateModel {

	// group name while creating and new groupname while updating
	private String groupName;
  private Date creationTime;
  private String groupCreator;
  //whether moderator approval required to add members
  private boolean isAuthRequired;
  //this flag differentiates between group creation and updation msgs
  private boolean isUpdate;
  private String currentGroupName;

  public GroupCreateUpdateModel(String grpName, String creatorName, boolean isAuthRequired) {
  	this.groupName = grpName;
  	this.groupCreator = creatorName;
  	this.isAuthRequired = isAuthRequired;
  }
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

  public String getGroupCreator() {
    return groupCreator;
  }

  public void setGroupCreator(String groupCreator) {
    this.groupCreator = groupCreator;
  }

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getCurrentGroupName() {
		return currentGroupName;
	}

	public void setCurrentGroupName(String currentGroupName) {
		this.currentGroupName = currentGroupName;
	}
  
}
