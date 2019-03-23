package edu.northeastern.ccs.im.business.logic;

import edu.northeastern.ccs.im.server.Connection;

public class GroupChatHandler implements MessageHandler {
  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {
    return false;
  }
}
