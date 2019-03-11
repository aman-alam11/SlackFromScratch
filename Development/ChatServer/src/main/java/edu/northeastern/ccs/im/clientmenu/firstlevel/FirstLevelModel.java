package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.List;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageJson;

public class FirstLevelModel implements Connection {


  @Override
  public boolean isConnected() {
    return false;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public MessageJson next() {
    return null;
  }

  @Override
  public boolean sendMessage(MessageJson message) {
    return false;
  }
}
