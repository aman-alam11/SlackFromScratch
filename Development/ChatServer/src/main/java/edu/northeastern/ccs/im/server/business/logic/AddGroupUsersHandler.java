package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.server.Connection;

public class AddGroupUsersHandler implements MessageHandler {
	
	private Gson gson;
	private AckModel ackModel;
	
	public AddGroupUsersHandler() {
		gson = new Gson();
		ackModel = new AckModel();
	}

	@Override
	public boolean handleMessage(String user, String message, Connection clientConnection) {
		//validate group
		
		//validate users
		
		//add users in group
		
		//send ack
		
		return false;
	}

}
