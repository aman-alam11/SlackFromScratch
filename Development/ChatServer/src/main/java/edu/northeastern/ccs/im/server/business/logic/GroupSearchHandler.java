package edu.northeastern.ccs.im.server.business.logic;

import java.util.List;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.Group;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.GroupSearchModel;
import edu.northeastern.ccs.im.server.Connection;

public class GroupSearchHandler implements MessageHandler {
	
	private Gson gson;
	
	public GroupSearchHandler() {
		gson = new  Gson();
	}

	@Override
	public boolean handleMessage(String user, String message, Connection clientConnection) {
		
		GroupSearchModel groupSearch = gson.fromJson(message, GroupSearchModel.class);
		List<Group> groupList = JPAService.getInstance().allGroupsForUser(user, groupSearch.getToChatWithGroupName());
		
		GroupSearchModel searchResult = new GroupSearchModel(groupSearch.getToChatWithGroupName());
		MessageJson msgJson = new MessageJson(MessageType.GROUP_SEARCH);
		if (groupList != null && !groupList.isEmpty()) {
			for (Group group : groupList) {
				searchResult.getListGroupString().add(group.getgName());
			}
		}
		msgJson.setMessage(gson.toJson(searchResult));
		sendResponse(msgJson, clientConnection);
		return true;
	}

}
