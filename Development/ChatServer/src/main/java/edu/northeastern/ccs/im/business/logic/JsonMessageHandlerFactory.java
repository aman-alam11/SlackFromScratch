package edu.northeastern.ccs.im.business.logic;

import edu.northeastern.ccs.im.message.MessageType;

/**
 * MessageHandler Factory which returns new instance of MessageHandler depending on the MessageType.
 * MessageType is an enum.
 */
public class JsonMessageHandlerFactory implements MessageHandlerFactory {

  @Override
  public MessageHandler getMessageHandler(MessageType msgType) {
    MessageHandler handler = null;
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
       
      case USER_SEARCH:
    	handler = new UserSearchHandler();
    	break;

      default:
        // Send to Login Page as default for safety
        handler = new LoginHandler();
        break;
    }
    return handler;
  }

}
