package edu.northeastern.ccs.im.model;

import java.util.List;

public class UserFollwingList {
	
	private List<User> userList;
	
	public UserFollwingList(List<User> userList) {
		this.userList = userList;
	}

	public List<User> getUserList() {
		return userList;
	}

}
