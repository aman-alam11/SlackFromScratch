package edu.northeastern.ccs.im.server.business.logic;

import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;

/**
 * Interface to handle different types of messages.
 */
public interface MessageHandler {

  /**
   * This method is used to handle the message that is being transmitted between the users
   * based on {@link MessageType}.
   * @param user
   * @param message
   * @param clientConnection
   * @return
   */
  boolean handleMessage(String user, String message, Connection clientConnection);

  default void sendResponse(MessageJson msg, Connection clientConnection) {
    clientConnection.enqueueMessage(msg);
  }


}
