package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;

public class RenameGroupHandler implements MessageHandler {

  private static final String LOG_TAG = RenameGroupHandler.class.getSimpleName();
  private JPAService mJpaService;

  public RenameGroupHandler() {
    mJpaService = JPAService.getInstance();
  }


  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {
    try {

      Type nameList = new TypeToken<List<String>>() {}.getType();
      List<String> lst = new Gson().fromJson(message, nameList);
      boolean isSuccess = mJpaService.renameUpdateGroup(lst.get(0), lst.get(1));

      MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
              MessageType.RENAME_GROUP, new Gson().toJson(isSuccess));

      sendResponse(response, clientConnection);
      return true;

    } catch (Exception e) {
      ChatLogger.error(LOG_TAG + " : " + e.getMessage());
      return false;
    }
  }
}
