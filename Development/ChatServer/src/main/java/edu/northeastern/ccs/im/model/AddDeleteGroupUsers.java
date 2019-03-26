package edu.northeastern.ccs.im.model;

import java.util.List;

public class AddDeleteGroupUsers {
	
	private String groupName;
	private List<String> usersList;
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public List<String> getUsersList() {
		return usersList;
	}
	public void setUsersList(List<String> usersList) {
		this.usersList = usersList;
	}
}
