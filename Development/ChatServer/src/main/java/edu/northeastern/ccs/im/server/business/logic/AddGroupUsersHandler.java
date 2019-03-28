package edu.northeastern.ccs.im.server.business.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.Group;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.AddDeleteGroupUsers;
import edu.northeastern.ccs.im.model.ErrorCodes;
import edu.northeastern.ccs.im.server.Connection;

public class AddGroupUsersHandler implements MessageHandler {
	
	private Gson gson;
	private AckModel ackModel;
	private List<User> validUsersToAdd;

	public AddGroupUsersHandler() {
		gson = new Gson();
		ackModel = new AckModel();
		validUsersToAdd = new ArrayList<>();
	}

	/**{@inheritDoc}
	 * Adds users provided in list to Group if at least one of the users is valid
	 */
	@Override
	public boolean handleMessage(String user, String message, Connection clientConnection) {
		
		AddDeleteGroupUsers usersModel = gson.fromJson(message, AddDeleteGroupUsers.class);
		
		if(validate(usersModel, user) && !validUsersToAdd.isEmpty()) {
			List<String> userNameToAdd = validUsersToAdd.stream()
																									 .map(u -> u.getName())
																									 .collect(Collectors.toList());
			JPAService.getInstance().addMultipleUsersToGroup(userNameToAdd, usersModel.getGroupName());
		}
		
		MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, gson.toJson(ackModel));
		sendResponse(response, clientConnection);
		return false;
	}
	
	private boolean validate(AddDeleteGroupUsers users, String requester) {
		return validateGroupAndRequestor(users.getGroupName(), requester) && 
						validateUsers(users.getUsersList(),users.getGroupName());
	}
	
	/**
	 * Checks if the group exists and the requester is in group
	 * @param grpName
	 * @return true if group exists else false
	 */
	private boolean validateGroupAndRequestor(String grpName, String requester) {
		boolean isValid = true;
		Group groupDBObject = JPAService.getInstance().findGroupByName(grpName);
		if (groupDBObject == null) {
			ackModel.addErrorCode(ErrorCodes.G803);
			ackModel.appendErrorMessage(" \n GroupName: " + grpName);
			isValid = false;
		} else {
			List<User> user = JPAService.getInstance().findNonMembers(Arrays.asList(requester), grpName);
			if (!user.isEmpty()) {
				ackModel.addErrorCode(ErrorCodes.G809);
				ackModel.appendErrorMessage(" \n Requester is not a member of Group " + grpName);
			}
		}
		return isValid;
	}
	
	private boolean validateUsers(List<String> users, String groupName) {
		boolean isValid = false;
		List<String> validUsers = new ArrayList<>();
		List<String> inValidUsers  = new ArrayList<>();
		for (String userName : users) {
			if (JPAService.getInstance().findUserByName(userName) == null) {
				inValidUsers.add(userName);
			} else {
				validUsers.add(userName);
			}
		}
		
		if (users.isEmpty()) {
			//if no users were provided to add
			ackModel.addErrorCode(ErrorCodes.G804);
			
		} else if (users.size() == inValidUsers.size()) {
			// if all users provided does not exists
			ackModel.addErrorCode(ErrorCodes.G808);
			
		} else if (!validUsers.isEmpty()) {
			isValid = checkUsersNotInGroup(validUsers, groupName);
			if (isValid && validUsersToAdd.size() != validUsers.size()) {
				ackModel.addErrorCode(ErrorCodes.G806);
				ackModel.appendErrorMessage(" \n Only Following Users were added in group :" + " : "
						+ gson.toJson(validUsersToAdd.stream().map(u -> u.getName()).collect(Collectors.toList())));
			}
		}
		return isValid;
	}
	
	private boolean checkUsersNotInGroup(List<String> usersList, String groupName) {
		boolean isValid = false;
		// if some of the users exists
		List<User> validUsersToAddToGrp = JPAService.getInstance().findNonMembers(usersList, groupName);
		if (!validUsersToAddToGrp.isEmpty()) {
			isValid = true;
			
			// users that can be added to group
			validUsersToAdd = validUsersToAddToGrp;
		} else {
			ackModel.addErrorCode(ErrorCodes.G807);
		}
		return isValid;
	}
}
