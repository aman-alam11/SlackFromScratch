package edu.northeastern.ccs.im.client.communication;

import edu.northeastern.ccs.im.message.MessageJson;

/**
 * The top layer of options we have in our menu in command prompt.
 */
public interface Connection {
	boolean isConnected();
	boolean hasNext();
	MessageJson next();
	boolean sendMessage(MessageJson message);
}
