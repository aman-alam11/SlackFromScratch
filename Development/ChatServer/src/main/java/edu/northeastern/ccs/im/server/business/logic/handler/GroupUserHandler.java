package edu.northeastern.ccs.im.server.business.logic.handler;

import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

public class GroupUserHandler implements MessageHandler {

	@Override
	public boolean handleMessage(String user, String message, Connection clientConnection) {
		return false;
	}

}
