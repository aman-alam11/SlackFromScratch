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

  private static final String INVALID_USERNAME_GROUPNAME = "Input can't be blank. Sending you back";
  private SuperUserMessageModel mSuperUserMessageModel;
  private String gsonSerialized;
  private String guideUserString;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {

    String username;

    FrontEnd.getView().showSuperUserOperations();
    int choice = Integer.parseInt(scanner.nextLine());
    switch (choice) {

      // TODO: Break this big switch case into smaller methods for all TT, TF, FT, FF truth table cases
      // TODO: Add comments
      // Get All Conversations for a particular User for user to user chat
      case 1:
        FrontEnd.getView().sendToView("Enter the name of user to tap into his/her user2user chats:");
        username = scanner.nextLine().trim().toLowerCase();
        if (StringUtil.isBlank(username)) {
          FrontEnd.getView().sendToView(INVALID_USERNAME_GROUPNAME);
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
          return;
        }

        mSuperUserMessageModel = new SuperUserMessageModel(true, false, username);
        updateDatesForRequest(scanner);
        gsonSerialized = new Gson().toJson(mSuperUserMessageModel);
        generateRequestForServer(connectionLayerModel);
        guideUserString = "Conversations for a particular User for user to user chat";
        break;

      // Get All Conversations for a particular User for all group chats
      case 2:
        FrontEnd.getView().sendToView("Enter the name of user to tap into his/her group2group chats:");
        username = scanner.nextLine().trim().toLowerCase();

        if (StringUtil.isBlank(username)) {
          FrontEnd.getView().sendToView(INVALID_USERNAME_GROUPNAME);
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
          return;
        }

        mSuperUserMessageModel = new SuperUserMessageModel(false, true, username);
        updateDatesForRequest(scanner);
        gsonSerialized = new Gson().toJson(mSuperUserMessageModel);
        guideUserString = "Conversations for a particular User for all group chats";
        generateRequestForServer(connectionLayerModel);
        break;

      // Get All Conversations for a user for both user to user chat and group
      // Basically all unread messages
      case 3:
        FrontEnd.getView().sendToView("Enter the name of user to tap into his/her user and group chats:");
        username = scanner.nextLine().trim().toLowerCase();
        if (StringUtil.isBlank(username)) {
          FrontEnd.getView().sendToView(INVALID_USERNAME_GROUPNAME);
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
          return;
        }

        mSuperUserMessageModel = new SuperUserMessageModel(true, true, username);
        updateDatesForRequest(scanner);
        gsonSerialized = new Gson().toJson(mSuperUserMessageModel);
        guideUserString = "Conversations for a user for both user to user chat and group";
        generateRequestForServer(connectionLayerModel);
        break;

      // Get All Conversations for a particular Group irrespective of user
      case 4:
        FrontEnd.getView().sendToView("Enter the group name for which you want to get all the conversations from:");
        String groupName = scanner.nextLine().trim().toLowerCase();
        if (StringUtil.isBlank(groupName)) {
          FrontEnd.getView().sendToView(INVALID_USERNAME_GROUPNAME);
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
          return;
        }

        mSuperUserMessageModel = new SuperUserMessageModel(groupName);
        updateDatesForRequest(scanner);
        gsonSerialized = new Gson().toJson(mSuperUserMessageModel);
        guideUserString = "Conversations for a particular Group irrespective of user";
        generateRequestForServer(connectionLayerModel);
        break;

      default:
        // send back to first level
        FrontEnd.getView().sendToView("Wrong option selected. Sending you back");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
    }
  }


  private void updateDatesForRequest(Scanner scanner) {
    FrontEnd.getView().sendToView("Do you want to get requested chats for specific dates " +
            "or do you want to get all dates of the requested types?");
    FrontEnd.getView().sendToView("Press 1 to get all requested chats irrespective of dates");
    FrontEnd.getView().sendToView("Press 2 to enter specific dates and get chats for those time frames");
    int choice = Integer.parseInt(scanner.nextLine().trim());
    if (choice == 2) {
      // Get Dates from user
      getDatesFromUser(scanner);
    }

    if (choice < 1 || choice > 2) {
      FrontEnd.getView().sendToView("Invalid input. Getting all chats as default.");
    }
  }

  private void getDatesFromUser(Scanner scanner) {
    FrontEnd.getView().sendToView("Dates entered should be of type: MM/DD/YYYY . You have to include \\/ in the date");
    FrontEnd.getView().sendToView("Please enter the start date to get chats:");
    String startDate = scanner.nextLine().trim();
    if (!invalidDateCheck(startDate, true)) {
      return;
    }

    FrontEnd.getView().sendToView("Please enter the end date to get chats:");
    String endDate = scanner.nextLine().trim();
    invalidDateCheck(endDate, false);
  }

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

  private void generateRequestForServer(Connection connectionLayerModel) {
    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
            MessageType.SUPER_USER, gsonSerialized);
    connectionLayerModel.sendMessage(messageJson);

    String resp = waitForResponseSocket(connectionLayerModel);
    if (!StringUtil.isBlank(resp)) {
      this.displayResponse(resp);
    } else {
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.DEFAULT_LEVEL);
    }
  }

  /**
   * Display the response or error code appropriately.
   *
   * @param resp The response recieved by server. This can either be of type {@code
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
      //
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
