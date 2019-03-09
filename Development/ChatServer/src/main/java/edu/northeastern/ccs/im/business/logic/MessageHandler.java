package edu.northeastern.ccs.im.business.logic;

import edu.northeastern.ccs.im.model.User;

public interface MessageHandler {
	
	boolean handleMessage(String user, String message);

}
