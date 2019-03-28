package edu.northeastern.ccs.im.server.business.logic;

import java.util.List;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.Group;
import edu.northeastern.ccs.im.database.JPAService;
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
		//List<Group> groupList = JPAService.getInstance().searchGroupByName(groupSearch.)
		return false;
	}

}
