package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;

public class DeleteGroupHandler implements MessageHandler {

  private static final String LOG_TAG = DeleteGroupHandler.class.getSimpleName();
  private JPAService mJpaService;

  public DeleteGroupHandler() {
    mJpaService = JPAService.getInstance();
  }


  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {

    try {
      boolean isOperationSuccessful = mJpaService.deleteGroup(message);

      MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
              MessageType.DELETE_GROUP, new Gson().toJson(isOperationSuccessful));

      sendResponse(response, clientConnection);
      return true;

    } catch (Exception e) {
      ChatLogger.error(LOG_TAG + " : " + e.getMessage());
      return false;
    }
  }
}
