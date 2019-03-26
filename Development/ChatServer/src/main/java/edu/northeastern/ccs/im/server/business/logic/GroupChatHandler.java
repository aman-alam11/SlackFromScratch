package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;

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
}
