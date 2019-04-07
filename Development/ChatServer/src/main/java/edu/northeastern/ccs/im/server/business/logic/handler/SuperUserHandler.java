package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.FetchLevel;
import edu.northeastern.ccs.im.model.SuperUserMessageModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

/**
 * Handler server side requests for super user related operations.
 */
public class SuperUserHandler implements MessageHandler {

  private static final String LOG_TAG = SuperUserHandler.class.getSimpleName();
  public static final String START_DATE = "START_DATE_KEY";
  public static final String END_DATE = "END_DATE_KEY";


  private final Gson mGson;
  private final JPAService mJpaService;
  private boolean areDatesValid;
  private SuperUserMessageModel superUserMessageModel;
  private HashMap<String, Date> dateMap;

  public SuperUserHandler() {
    mGson = new Gson();
    mJpaService = JPAService.getInstance();
  }

  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {
    try {

      superUserMessageModel = new Gson().fromJson(message, SuperUserMessageModel.class);
      areDatesValid = superUserMessageModel.areDatesValid();
      handleChatsHelper();

//      MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.UNREAD_MSG,
//              mGson.toJson(unreadMessages));
//      sendResponse(response, clientConnection);
      return true;

    } catch (Exception e) {
      ChatLogger.error(LOG_TAG + " : " + e.getMessage());
      return false;
    }
  }

  // A helper to dispatch work to appropriate method depending on whether we want all chats or just
  // user2user chats group chats for user
  private void handleChatsHelper() {
      if(superUserMessageModel.isGetAllChats()) {
          handleGetAllChats(superUserMessageModel.getmGroupToTap());
      } else if(superUserMessageModel.isGetOnlyUserChat()) {
          handleGetOnlyUserChats(superUserMessageModel.getmUsernameToTap());
      } else if (superUserMessageModel.isGetOnlyGroupChat()) {
          handleGetOnlyGroupChatForDates(superUserMessageModel.getmUsernameToTap());
      }
  }


  private void handleGetOnlyGroupChatForDates(String userName) {
    if(superUserMessageModel.areDatesValid()) {
      updateDateMap();
      mJpaService.getUnreadMessages(userName, dateMap, FetchLevel.FETCH_GROUP_LEVEL);
    } else {
      mJpaService.getUnreadMessages(userName, null, FetchLevel.FETCH_GROUP_LEVEL);
    }
  }



  private void handleGetOnlyUserChats(String userName) {
    if(superUserMessageModel.areDatesValid()) {
      updateDateMap();
      mJpaService.getUnreadMessages(userName, dateMap, FetchLevel.FETCH_USER_LEVEL);
    } else {
      mJpaService.getUnreadMessages(userName, null, FetchLevel.FETCH_USER_LEVEL);
    }
  }

  private void handleGetAllChats(String groupName) {
    if(superUserMessageModel.areDatesValid()) {
      updateDateMap();
      mJpaService.getUnreadMessagesForGroup(groupName, dateMap);
    } else {
      mJpaService.getUnreadMessagesForGroup(groupName, null);
    }
  }

  private void updateDateMap() {
    Date startDate = superUserMessageModel.getStartDate();
    Date endDate = superUserMessageModel.getEndDate();
    dateMap = new HashMap<>();
    dateMap.put(START_DATE, startDate);
    dateMap.put(END_DATE, endDate);
  }
}
