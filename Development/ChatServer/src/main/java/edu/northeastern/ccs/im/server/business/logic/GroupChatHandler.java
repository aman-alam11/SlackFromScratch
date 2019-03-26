package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.model.GroupCreateUpdateModel;
import edu.northeastern.ccs.im.server.Connection;

public class GroupChatHandler implements MessageHandler {
	
	private Gson gson;
	
	public GroupChatHandler() {
		gson = new Gson();
	}
  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {
  	
    return false;
  }
}
