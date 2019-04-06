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
import edu.northeastern.ccs.im.clientmenu.userlevel.SuperUserMessageModel;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UnreadMessageModel;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

/**
 * A class for all the super user options.
 */
public class SuperUser implements CoreOperation {

  private static final String INVALID_USERNAME_GROUPNAME = "Input can't be blank. Sending you back";
  SuperUserMessageModel superUserMessageModel;
  private String gsonSerialized;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {

    String username;

    FrontEnd.getView().showSuperUserOperations();
    int choice = Integer.parseInt(scanner.nextLine());
    switch (choice) {
      // TODO: Break this big switch case into smaller methods for all TT, TF, FT, FF truth table cases
      // Get All Conversations for a particular User for user to user chat
      case 1:
        username = scanner.nextLine().trim().toLowerCase();
        if (StringUtil.isBlank(username)) {
          FrontEnd.getView().sendToView(INVALID_USERNAME_GROUPNAME);
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
          return;
        }

        superUserMessageModel = new SuperUserMessageModel(true, false, username);
        updateDatesForRequest(scanner);
        gsonSerialized = new Gson().toJson(superUserMessageModel);
        generateRequestForServer(connectionLayerModel);
        break;

      // Get All Conversations for a particular User for all group chats
      case 2:
        username = scanner.nextLine().trim().toLowerCase();

        if (StringUtil.isBlank(username)) {
          FrontEnd.getView().sendToView(INVALID_USERNAME_GROUPNAME);
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
          return;
        }

        superUserMessageModel = new SuperUserMessageModel(false, true, username);
        updateDatesForRequest(scanner);
        gsonSerialized = new Gson().toJson(superUserMessageModel);
        break;

      // Get All Conversations for a user for both user to user chat and group
      // Basically all unread messages
      case 3:
        username = scanner.nextLine().trim().toLowerCase();
        if (StringUtil.isBlank(username)) {
          FrontEnd.getView().sendToView(INVALID_USERNAME_GROUPNAME);
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
          return;
        }

        superUserMessageModel = new SuperUserMessageModel(true, true, username);
        updateDatesForRequest(scanner);
        gsonSerialized = new Gson().toJson(superUserMessageModel);
        break;

      // Get All Conversations for a particular Group irrespective of user
      case 4:
        String groupName = scanner.nextLine().trim().toLowerCase();
        if (StringUtil.isBlank(groupName)) {
          FrontEnd.getView().sendToView(INVALID_USERNAME_GROUPNAME);
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
          return;
        }

        superUserMessageModel = new SuperUserMessageModel(true, groupName);
        updateDatesForRequest(scanner);
        gsonSerialized = new Gson().toJson(superUserMessageModel);
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
    int choice  = Integer.parseInt(scanner.nextLine());
    if (choice == 2) {
      // Get Dates from user
      getDatesFromUser(scanner);
    }

    if(choice != 1 && choice != 2) {
      FrontEnd.getView().sendToView("Invalid input. Getting all chats as default.");
    }
  }

  private void getDatesFromUser(Scanner scanner) {
    FrontEnd.getView().sendToView("Dates entered should be of type: MM/DD/YYYY");
    FrontEnd.getView().sendToView("Please enter the start date to get chats:");
    String startDate = scanner.nextLine().trim();
     if(invalidDateCheck(startDate, true)) {
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
      if(isStartDate) {
        superUserMessageModel.setmStartDate(date);
      } else {
        if (superUserMessageModel.getStartDate().after(date)) {
          // End date is still invalid
          FrontEnd.getView().sendToView("Invalid date entered. Getting all chats of requested type");
          superUserMessageModel.setAreDatesValid(false);
          return false;
        } else {
          superUserMessageModel.setmEndDate(date);
          // Both start and end dates are valid now
          superUserMessageModel.setAreDatesValid(true);
        }
      }
      return true;
    } catch (ParseException e) {
      superUserMessageModel.setAreDatesValid(false);
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

  private void displayResponse(String resp) {
    Type chatModelList = new TypeToken<List<UnreadMessageModel>>() {
    }.getType();
    List<UnreadMessageModel> listUnreadMessagesAll = new Gson().fromJson(resp, chatModelList);

    for (UnreadMessageModel eachChat : listUnreadMessagesAll) {
        FrontEnd.getView().sendToView(eachChat.toString());
    }

    // TODO: Display each case properly
//    if (!listUnreadMessagesAll.isEmpty()) {
//
//    } else {
//      FrontEnd.getView().sendToView("INFO: No new messages");
//    }
  }

}
