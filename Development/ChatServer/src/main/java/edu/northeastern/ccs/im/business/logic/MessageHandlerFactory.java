package edu.northeastern.ccs.im.business.logic;

import edu.northeastern.ccs.im.message.MessageType;

/**
 * MessageHandler factory interface.
 */
public interface MessageHandlerFactory {
	MessageHandler getMessageHandler(MessageType msgType);
}
