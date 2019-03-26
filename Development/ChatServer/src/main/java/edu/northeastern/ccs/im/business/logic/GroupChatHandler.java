package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.Group;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.GroupModel;
import edu.northeastern.ccs.im.server.Connection;

public class GroupChatHandler implements MessageHandler {
	
	private Gson gson;
	
	public GroupChatHandler() {
		gson = new Gson();
	}
  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {
  	GroupModel groupCreateMsg = gson.fromJson(message, GroupModel.class);
    return false;
  }
  
  private boolean validateGroupName(GroupModel grp) {
  	String grpName = grp.getGroupName();
  	Group groupSearch = JPAService.getInstance().findGroupByName(grpName);
  	return groupSearch == null;
  }
}
