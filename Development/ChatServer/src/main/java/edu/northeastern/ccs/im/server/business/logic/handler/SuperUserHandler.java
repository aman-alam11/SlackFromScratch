package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.ErrorCodes;
import edu.northeastern.ccs.im.model.FetchLevel;
import edu.northeastern.ccs.im.model.SuperUserMessageModel;
import edu.northeastern.ccs.im.model.UnreadMessageModel;
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
  private SuperUserMessageModel superUserMessageModel;
  private HashMap<String, String> dateMap;
  private Connection mConnection;

  public SuperUserHandler() {
    mGson = new Gson();
    mJpaService = JPAService.getInstance();
  }

  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {
    try {
      this.mConnection = clientConnection;
      superUserMessageModel = new Gson().fromJson(message, SuperUserMessageModel.class);

      // First check if the sender is a super user
      // This is important if someone somehow fakes that s/he is a superuser on client side.
      if (!mJpaService.findUserByName(user).isSuperUser()) {
        // If not a super user, simply send blank response
        AckModel ackModel = new AckModel();
        ackModel.addErrorCode(ErrorCodes.ILGL);
        MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.SUPER_USER,
                mGson.toJson(ackModel));
        sendResponse(response, clientConnection);
        return false;
      }


      handleChatsHelper();

      return true;

    } catch (Exception e) {
      ChatLogger.error(LOG_TAG + " : " + e.getMessage());
      return false;
    }
  }

  /**
   * A helper to dispatch work to appropriate method depending on whether we want all chats or just
   * user2user chats group chats for user.
   */
  private void handleChatsHelper() {
    if (superUserMessageModel.isGetAllChats()) {
      handleChatsUsername(superUserMessageModel.getmUsernameToTap(), FetchLevel.FETCH_BOTH_USER_GROUP_LEVEL);
    } else if (superUserMessageModel.isGetOnlyUserChat()) {
      handleChatsUsername(superUserMessageModel.getmUsernameToTap(), FetchLevel.FETCH_USER_LEVEL);
    } else if (superUserMessageModel.isGetOnlyGroupChat()) {
      handleChatsUsername(superUserMessageModel.getmUsernameToTap(), FetchLevel.FETCH_GROUP_LEVEL);
    } else {
      handleGetAllChatsForGroupName(superUserMessageModel.getmGroupToTap());
    }
  }

  /**
   * Handle messages of type related to usernames. This includes 3 types: User 2 User chats, all
   * Group chats for a user for all the groups s/he might be in and lastly all messages irrespective
   * of the message is of type user 2 user or group message for a particular user.
   *
   * @param userName   The username of the user we want to fetch messages for.
   * @param fetchLevel The type of messages we want to fetch is based on the Fetch Levels. The types
   *                   are as described in Javadoc description of this method.
   */
  private void handleChatsUsername(String userName, FetchLevel fetchLevel) {
    List<UnreadMessageModel> unreadMessageList;
    if (superUserMessageModel.areDatesValid()) {
      updateDateMap();
      unreadMessageList = mJpaService.getUnreadMessages(userName, dateMap, fetchLevel);
    } else {
      unreadMessageList = mJpaService.getUnreadMessages(userName, null, fetchLevel);
    }
    sendResponseBack(unreadMessageList);
  }


  /**
   * Get all chats for a group irrespective of user.
   *
   * @param groupName The group for which we need to fetch messages for.
   */
  private void handleGetAllChatsForGroupName(String groupName) {
    List<UnreadMessageModel> unreadMessageList;
    if (superUserMessageModel.areDatesValid()) {
      updateDateMap();
      unreadMessageList = mJpaService.getUnreadMessagesForGroup(groupName, dateMap);
    } else {
      unreadMessageList = mJpaService.getUnreadMessagesForGroup(groupName, null);
    }
    sendResponseBack(unreadMessageList);
  }

  /**
   * A helper method to simply get the start and end date from the passed model and add it to a map
   * for passing. A simple way would have been to use a Pair here but since our jenkins has some
   * issue and is unable to find it, we have shifted to Map (which is unnecessary here).
   */
  private void updateDateMap() {
    dateMap = new HashMap<>();
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Date startDate = simpleDateFormat.parse(simpleDateFormat.format(superUserMessageModel.getStartDate()));
      Date endDate = simpleDateFormat.parse(simpleDateFormat.format(superUserMessageModel.getEndDate()));
      dateMap.put(START_DATE, String.valueOf(startDate.toInstant().atZone(ZoneId.of("America/Montreal")).toLocalDate()));
      dateMap.put(END_DATE, String.valueOf(endDate.toInstant().atZone(ZoneId.of("America/Montreal")).toLocalDate()));
    } catch (Exception e) {
      ChatLogger.error("Unable to parse dates in " + LOG_TAG + " updateDateMap");
    }
  }

  /**
   * A helper method to send response back to client.
   *
   * @param resp the list of messages according to the request made by superuser.
   */
  private void sendResponseBack(List<UnreadMessageModel> resp) {
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.SUPER_USER,
            mGson.toJson(resp));
    sendResponse(response, mConnection);
  }
}
