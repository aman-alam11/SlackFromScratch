package edu.northeastern.ccs.im.business.logic;

import edu.northeastern.ccs.im.message.MessageType;

public class JsonMessageHandlerFactory implements MessageHandlerFactory {

	@Override
	public MessageHandler getMessageHandler(MessageType msgType) {
		MessageHandler handler =null;
		switch (msgType) {
		case CREATE_USER:
			handler = new UserCreationHandler();
			break;
		case LOGIN:
			handler = new LoginHandler();
			break;

			case USER_CHAT:
				handler = new ChatHandler();
				break;

		default:
			break;
		}
		return handler;
	}
	
}
