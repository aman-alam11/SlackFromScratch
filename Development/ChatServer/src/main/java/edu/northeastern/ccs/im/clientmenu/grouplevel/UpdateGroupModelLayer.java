package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentGroupName;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class UpdateGroupModelLayer implements CoreOperation {
  private Scanner mScanner;
  private Connection mConnectionLayerModel;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    this.mScanner = scanner;
    mConnectionLayerModel = connectionLayerModel;

    FrontEnd.getView().sendToView("Let's see how many groups you belong to; " +
            "Fetching all your groups, please wait");

    modifyGroup(GetAllGroupsUtil.parseResponse(MessageType.GET_ALL_GROUPS_MOD, connectionLayerModel));
  }


  private void modifyGroup(Map<String, Boolean> listGroupMod) {
    if (checkForNullEmpty(listGroupMod)) {
      return;
    }

    FrontEnd.getView().sendToView("Enter name of the group you want to modify from above:");
    String groupName = mScanner.nextLine().trim();
    boolean noGroupMatched = true;

    for (Map.Entry<String, Boolean> eachGroupAndModPair : listGroupMod.entrySet()) {
      if (eachGroupAndModPair.getKey().equals(groupName)) {
        noGroupMatched = false;
        // If current user is moderator
        if (eachGroupAndModPair.getValue()) {
          FrontEnd.getView().showUpdateGroupOptions();
          String addDeleteChoice = mScanner.nextLine().trim();
          int choice = Integer.parseInt(addDeleteChoice);
          if (choice == 1) {
            CurrentGroupName.setGroupName(eachGroupAndModPair.getKey());
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_USERS_CRUD_LEVEL);
          } else if (choice == 2) {
            CurrentGroupName.setGroupName(eachGroupAndModPair.getKey());
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_USERS_MODERATOR_OPERATIONS);
          } else if (choice == 3) {
            updateGroupName(eachGroupAndModPair.getKey());
          } else {
            FrontEnd.getView().sendToView("Illegal Input entered. Sending you back.");
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
          }
        } else {
          FrontEnd.getView().sendToView("You do not have rights to modify that group. Sending you back");
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
        }
      }
    }

    if (noGroupMatched) {
      FrontEnd.getView().sendToView("Illegal Group Name entered.");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
    }
  }

  private void updateGroupName(String currentGroupName) {
    FrontEnd.getView().sendToView("Enter the new name of group: " + currentGroupName);
    String newName = mScanner.nextLine().trim();
    if(StringUtil.isBlank(newName)) {
      FrontEnd.getView().sendToView("Name has to have at least 1 letter");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      return;
    }

    if(newName.equalsIgnoreCase(currentGroupName)){
      FrontEnd.getView().sendToView("No update required as names are same.\n");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
    } else {
      List<String> listName = new ArrayList<>();
      listName.add(currentGroupName);
      listName.add(newName);
      MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
              MessageType.RENAME_GROUP, new Gson().toJson(listName));
      mConnectionLayerModel.sendMessage(messageJson);
      String response = waitForResponseSocket(mConnectionLayerModel);
      if (response.equalsIgnoreCase("true")) {
        FrontEnd.getView().sendToView("Renamed Group to " + newName);
      } else {
        FrontEnd.getView().sendToView("Uh Oh! Unable to rename group, Let's try again!");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      }

      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
    }
  }

  private boolean checkForNullEmpty(Map<String, Boolean> listGroupMod) {
    if (listGroupMod == null || listGroupMod.isEmpty()) {
      FrontEnd.getView().sendToViewSameLine("Something went wrong. Please try again!");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      return true;
    }
    return false;
  }

}
