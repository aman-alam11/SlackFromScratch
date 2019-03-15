package edu.northeastern.ccs.im.clientmenu.clientutils;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.client.communication.AsyncListener;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;

public class AuthFlag implements AsyncListener {

  private static boolean mIsUserAuthenticated = false;

  public AuthFlag(boolean isUserAuthenticated) {
    mIsUserAuthenticated = isUserAuthenticated;
  }


  public boolean isUserAuthenticated() {
    return mIsUserAuthenticated;
  }

  public void resetFlags() {
    mIsUserAuthenticated = false;
  }


  public void reAuthenticateWithHandshake(MessageJson messageJson, Connection serverConnection) {
    if (messageJson.getMessageType().equals(MessageType.LOGIN)
            || messageJson.getMessageType().equals(MessageType.CREATE_USER)) {
      serverConnection.registerListener(this, MessageType.AUTH_ACK);
      serverConnection.sendMessage(messageJson);
    }
  }

  @Override
  public void listen(String message) {
    AckModel ackModel = new Gson().fromJson(message, AckModel.class);
    mIsUserAuthenticated = ackModel.isUserAuthenticated();
  }
}
