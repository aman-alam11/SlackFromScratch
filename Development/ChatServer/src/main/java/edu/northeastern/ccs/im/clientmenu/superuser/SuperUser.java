package edu.northeastern.ccs.im.clientmenu.superuser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.helper.StringUtil;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.SuperUserMessageModel;
import edu.northeastern.ccs.im.model.UnreadMessageModel;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

/**
 * A class for all the super user options.
 */
public class SuperUser implements CoreOperation {

  private static final String INVALID_USERNAME_GROUP_NAME = "Input can't be blank. Sending you back";
  private static final String USER_2_USER_USERNAME_REQUEST = "Enter the name of user to tap into his/her user2user chats:";
  private static final String GROUP_CHAT_USERNAME_REQUEST = "Enter the name of user to tap into his/her group2group chats:";
  private static final String USER_GROUP_CHAT_USERNAME_REQUEST = "Enter the name of user to tap into his/her user and group chats:";
  private static final String GROUP_CHAT_GROUPNAME_REQUEST = "Enter the group name for which you want to get all the conversations from:";

  private static final String GUIDE_STRING_USER_2_USER = "Conversations for a particular User for user to user chat";
  private static final String GUIDE_STRING_USERNAME_GROUP = "Conversations for a particular User for all group chats";
  private static final String GUIDE_STRING_USER_AND_GROUP = "Conversations for a user for both user to user chat and group";
  private static final String GUIDE_STRING_GROUPNAME_CRITERIA = "Conversations for a particular Group irrespective of user";

  private SuperUserMessageModel mSuperUserMessageModel;
  private String mGsonSerialized;
  private String guideUserString;
  private Scanner mScanner;
  private Connection mConnectionLayer;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {

    mScanner = scanner;
    this.mConnectionLayer = connectionLayerModel;

    FrontEnd.getView().showSuperUserOperations();
    int choice = Integer.parseInt(scanner.nextLine());
    switch (choice) {

      // Get All Conversations for a particular User for user to user chat
      case 1:
        guideUserString = GUIDE_STRING_USER_2_USER;
        requestChatsForUsernameCriteria(USER_2_USER_USERNAME_REQUEST,
                true, false, false);
        break;

      // Get All Conversations for a particular User for all group chats
      case 2:
        guideUserString = GUIDE_STRING_USERNAME_GROUP;
        requestChatsForUsernameCriteria(GROUP_CHAT_USERNAME_REQUEST,
                false, true, false);
        break;

      // Get All Conversations for a user for both user to user chat and group
      // Basically all unread messages
      case 3:
        guideUserString = GUIDE_STRING_USER_AND_GROUP;
        requestChatsForUsernameCriteria(USER_GROUP_CHAT_USERNAME_REQUEST,
                true, true, false);
        break;

      // Get All Conversations for a particular Group irrespective of user
      case 4:
        guideUserString = GUIDE_STRING_GROUPNAME_CRITERIA;
        requestChatsForUsernameCriteria(GROUP_CHAT_GROUPNAME_REQUEST,
                true, true, true);
        break;

      default:
        // send back to first level
        FrontEnd.getView().sendToView("Wrong option selected. Sending you back");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
    }
  }

  /**
   * Request messages when a username is involved in the message requests. This includes 3 of the 4
   * cases.
   *
   * @param frontEndMessageInitial  The Initial front end message that we want to show the user and
   *                                ask for username.
   * @param getOnlyUserChat         The criteria based on the case whether we just want user 2 user
   *                                chats.
   * @param getOnlyGroupChat        The criteria based on the case whether we just want group chats
   *                                for a particular user.
   * @param isGroupCriteriaInvolved The criteria which decides whether we have to fetch based on
   *                                username or group name and calls a different super user
   *                                constructor.
   */
  private void requestChatsForUsernameCriteria(String frontEndMessageInitial, boolean getOnlyUserChat,
                                               boolean getOnlyGroupChat, boolean isGroupCriteriaInvolved) {
    String nameInvolved;
    FrontEnd.getView().sendToView(frontEndMessageInitial);
    nameInvolved = mScanner.nextLine().trim().toLowerCase();

    if (StringUtil.isBlank(nameInvolved)) {
      FrontEnd.getView().sendToView(INVALID_USERNAME_GROUP_NAME);
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
      return;
    }

    if (isGroupCriteriaInvolved) {
      mSuperUserMessageModel = new SuperUserMessageModel(nameInvolved);
    } else {
      mSuperUserMessageModel = new SuperUserMessageModel(getOnlyUserChat, getOnlyGroupChat, nameInvolved);
    }
    updateDatesForRequest();
    mGsonSerialized = new Gson().toJson(mSuperUserMessageModel);
    generateRequestForServer();
  }


  /**
   * If a user wants to get messages for a time frame, request the start and end date.
   */
  private void updateDatesForRequest() {
    FrontEnd.getView().sendToView("Do you want to get requested chats for specific dates " +
            "or do you want to get all dates of the requested types?");
    FrontEnd.getView().sendToView("Press 1 to get all requested chats irrespective of dates");
    FrontEnd.getView().sendToView("Press 2 to enter specific dates and get chats for those time frames");
    int choice = Integer.parseInt(mScanner.nextLine().trim());
    if (choice == 2) {
      // Get Dates from user
      getDatesFromUser();
    }

    if (choice < 1 || choice > 2) {
      FrontEnd.getView().sendToView("Invalid input. Getting all chats as default.");
    }
  }

  /**
   * Request user for start and end date if user wants messages for a certain time period.
   */
  private void getDatesFromUser() {
    FrontEnd.getView().sendToView("Dates entered should be of type: MM/DD/YYYY ." +
            " You have to include \\/ in the date. 2 things to note: \n 1) The start date is " +
            "inclusive and end date is exclusive \n 2) Adding invalid dates lead to removal of date " +
            "criteria.");
    FrontEnd.getView().sendToView("Please enter the start date to get chats:");
    String startDate = mScanner.nextLine().trim();
    if (!invalidDateCheck(startDate, true)) {
      FrontEnd.getView().sendToView("Invalid Date Entered. Date criteria removed");
      return;
    }

    FrontEnd.getView().sendToView("Please enter the end date to get chats:");
    String endDate = mScanner.nextLine().trim();
    invalidDateCheck(endDate, false);
  }

  /**
   * A check for valid date. This check includes whether the date is entered in correct format. This
   * also checks whether the start date is before end date.
   *
   * @param enteredDate The date which can be start date as well as end date.
   * @param isStartDate The criteria which tells if the date entered is a start date or end date.
   *                    More checks are added if it is an end date.
   * @return A boolean which tells if all criteria for dates are met.
   */
  private boolean invalidDateCheck(String enteredDate, boolean isStartDate) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    try {
      Date date = simpleDateFormat.parse(enteredDate);
      if (isStartDate) {
        mSuperUserMessageModel.setmStartDate(date);
      } else {
        if (mSuperUserMessageModel.getStartDate().after(date)) {
          // End date is still invalid
          FrontEnd.getView().sendToView("Invalid date entered. Getting all chats of requested type");
          mSuperUserMessageModel.setAreDatesValid(false);
          return false;
        } else {
          mSuperUserMessageModel.setmEndDate(date);
          // Both start and end dates are valid now
          mSuperUserMessageModel.setAreDatesValid(true);
        }
      }
      return true;
    } catch (ParseException e) {
      mSuperUserMessageModel.setAreDatesValid(false);
      return false;
    }
  }

  /**
   * Send the request for messages to server and wait.
   */
  private void generateRequestForServer() {
    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
            MessageType.SUPER_USER, mGsonSerialized);
    mConnectionLayer.sendMessage(messageJson);

    String resp = waitForResponseSocket(mConnectionLayer);
    if (!StringUtil.isBlank(resp)) {
      this.displayResponse(resp);
    } else {
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.DEFAULT_LEVEL);
    }
  }

  /**
   * Display the response or error code appropriately.
   *
   * @param resp The response received by server. This can either be of type {@code
   *             List<UnreadMessageModel>} or a string which can be accessed via {@link
   *             AckModel#getErrorMessage()}.
   */
  private void displayResponse(String resp) {
    Type chatModelList = new TypeToken<List<UnreadMessageModel>>() {
    }.getType();
    List<UnreadMessageModel> listUnreadMessagesAll;

    try {
      listUnreadMessagesAll = new Gson().fromJson(resp, chatModelList);
    } catch (Exception e) {
      AckModel ackModel = new Gson().fromJson(resp, AckModel.class);
      FrontEnd.getView().sendToView(ackModel.getErrorMessage());
      return;
    }

    if (listUnreadMessagesAll.isEmpty()) {
      FrontEnd.getView().sendToView("No such results found. Sending you back");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
      return;
    }

    FrontEnd.getView().sendToView(guideUserString);

    for (UnreadMessageModel eachChat : listUnreadMessagesAll) {
      FrontEnd.getView().sendToView(eachChat.toString());
    }

    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
  }

}
