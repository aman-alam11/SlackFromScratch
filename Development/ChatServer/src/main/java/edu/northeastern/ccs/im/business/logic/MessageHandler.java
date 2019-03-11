package edu.northeastern.ccs.im.business.logic;

import edu.northeastern.ccs.im.database.JPAService;

public interface MessageHandler {
	
	boolean handleMessage(String user, String message);

	// TODO: ??
	// boolean handleMessage(String user, String message, JPAService jpaService);
}
