package edu.northeastern.ccs.im.server;

import edu.northeastern.ccs.im.message.MessageJson;

public interface Connection extends Runnable {

	void enqueueMessage(MessageJson message);
	String getUserName();
	boolean isAuthenticated();
	
}
