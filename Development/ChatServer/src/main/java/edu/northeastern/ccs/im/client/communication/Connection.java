package edu.northeastern.ccs.im.client.communication;

import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;

/**
 * The top layer of options we have in our menu in command prompt.
 */
public interface Connection {

  boolean isConnected();

  boolean hasNext();

  MessageJson next();

  boolean sendMessage(MessageJson message);

  void registerListener(AsyncListener asyncListener, MessageType messageType);

  void terminate();

}
