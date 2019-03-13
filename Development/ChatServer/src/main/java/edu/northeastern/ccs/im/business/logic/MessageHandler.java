package edu.northeastern.ccs.im.business.logic;

import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.server.Connection;

public interface MessageHandler {

  boolean handleMessage(String user, String message, Connection clientConnection);

  default void sendResponse(MessageJson msg, Connection clientConnection) {
    clientConnection.enqueueMessage(msg);
  }


}
