package edu.northeastern.ccs.im.server.business.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.Group;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.AddDeleteGroupUsers;
import edu.northeastern.ccs.im.model.ErrorCodes;
import edu.northeastern.ccs.im.server.Connection;

public class AddGroupUsersHandler implements MessageHandler {
	
	private Gson gson;
	private AckModel ackModel;
	private List<String> inValidUsers;
	private List<User> validUsers;
	Group groupDBObject;
	
	public AddGroupUsersHandler() {
		gson = new Gson();
		ackModel = new AckModel();
		inValidUsers = new ArrayList<>();
		validUsers = new ArrayList<>();;
	}

	/**{@inheritDoc}
	 * Adds users provided in list to Group if at least one of the users is valid
	 */
	@Override
	public boolean handleMessage(String user, String message, Connection clientConnection) {
		
		AddDeleteGroupUsers usersModel = gson.fromJson(message, AddDeleteGroupUsers.class);
		
		//validate 
		if(validate(usersModel)) {
			
		}
		
		//add users in group
		
		//send ack
		
		return false;
	}
	
	private boolean validate(AddDeleteGroupUsers users) {
		
		return validateGroup(users.getGroupName()) && validateUsers(users.getUsersList());
	}
	
	/**
	 * Checks if the group exists
	 * @param grpName
	 * @return true if grp exists else false
	 */
	private boolean validateGroup(String grpName) {
		
		groupDBObject = JPAService.getInstance().findGroupByName(grpName);
		boolean isValid = groupDBObject != null;
		if (!isValid) {
			ackModel.addErrorCode(ErrorCodes.G803);
			ackModel.appendErrorMessage(" \n GroupName: " + grpName);
		}
		return isValid;
	}
	
	private boolean validateUsers(List<String> users) {
		User user = null;
		boolean isValid = false;
		for (String userName : users) {
			
			if ((user =JPAService.getInstance().findUserByName(userName)) == null) {
				inValidUsers.add(userName);
			} else {
				validUsers.add(user);
			}
		}
		
		if (users.isEmpty()) {
			//if no users were provided to add
			ackModel.addErrorCode(ErrorCodes.G804);
			
		} else if (users.size() == inValidUsers.size()) {
			// if all users provided does not exists
			ackModel.addErrorCode(ErrorCodes.G808);
			
		} else if (!validUsers.isEmpty()) {
			// if some of the users exists
			List<User> validUsersToAddToGrp = checkUsersNotInGroup(validUsers);
			isValid = true;
			ackModel.addErrorCode(ErrorCodes.G806);
			ackModel.appendErrorMessage(" \n Only Following Users were added in group :" 
					+ " : " + gson.toJson(validUsersToAddToGrp.stream().map(u -> u.getName()).collect(Collectors.toList())));
			// users that can be added to group
			validUsers = validUsersToAddToGrp;
			
		}
		return isValid;
	}
	
	private List<User> checkUsersNotInGroup(List<User> usersList) {
		List<User> notInGroupUsers = new ArrayList<>();
		for (User user : usersList) {
			//get all users which does not exixts in group
		}
		
		return notInGroupUsers;
	}

}
