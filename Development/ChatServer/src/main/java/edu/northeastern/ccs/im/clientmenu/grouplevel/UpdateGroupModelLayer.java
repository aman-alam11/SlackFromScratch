package edu.northeastern.ccs.im.clientmenu.grouplevel;

import java.util.Map;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentGroupName;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UpdateGroupModelLayer implements CoreOperation {
  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    this.mScanner = scanner;

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
          FrontEnd.getView().sendToView("Press 1 to add/delete users and 2 to add/delete moderators?");
          String addDeleteChoice = mScanner.nextLine().trim();
          int choice = Integer.parseInt(addDeleteChoice);
          if (choice == 1) {
            CurrentGroupName.setGroupName(eachGroupAndModPair.getKey());
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_USERS_CRUD_LEVEL);
          } else if (choice == 2) {
            CurrentGroupName.setGroupName(eachGroupAndModPair.getKey());
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_USERS_MODERATOR_OPERATIONS);
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

  private boolean checkForNullEmpty(Map<String, Boolean> listGroupMod) {
    if (listGroupMod == null || listGroupMod.isEmpty()) {
      FrontEnd.getView().sendToViewSameLine("Something went wrong. Please try again!");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      return true;
    }
    return false;
  }

}
