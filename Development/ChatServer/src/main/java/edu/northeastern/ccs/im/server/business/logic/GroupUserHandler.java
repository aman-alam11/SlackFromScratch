package edu.northeastern.ccs.im.server.business.logic;

import edu.northeastern.ccs.im.server.Connection;

public class GroupUserHandler implements MessageHandler{

	@Override
	public boolean handleMessage(String user, String message, Connection clientConnection) {
		return false;
	}

}
