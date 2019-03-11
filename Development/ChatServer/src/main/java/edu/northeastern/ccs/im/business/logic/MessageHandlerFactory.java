package edu.northeastern.ccs.im.business.logic;

import edu.northeastern.ccs.im.message.MessageType;

public interface MessageHandlerFactory {
	MessageHandler getMessageHandler(MessageType msgType);

}
