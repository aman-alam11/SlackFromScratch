package edu.northeastern.ccs.im.clientmenu.grouplevel;

import java.util.Map;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.grouplevel.GetAllGroupsUtil.getAllUserGroup;
import static edu.northeastern.ccs.im.clientmenu.grouplevel.GetAllGroupsUtil.userToToggleModeratorOrDeleteUserGroup;

public class DeleteUsersFromGroup implements CoreOperation {

  private Scanner mScanner;
  private Connection mConnectionLayerModel;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    mScanner = scanner;
    mConnectionLayerModel = connectionLayerModel;

    parseResponse(getAllUserGroup(connectionLayerModel));
  }

  private void parseResponse(Map<String, Boolean> userModMap) {
    if (oneMemberCheckDeleteUser(userModMap)) {
      return;
    }

    FrontEnd.getView().sendToView("INPUT: Which user from above do you want to remove from the group?");
    String userToDelete = mScanner.nextLine().trim();

    if (!userModMap.containsKey(userToDelete)) {
      FrontEnd.getView().sendToView("ERROR: Illegal Name Entered. Sending you back");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
    } else {
      // Delete User from Group
      String responseBoolean = userToToggleModeratorOrDeleteUserGroup(userToDelete, mConnectionLayerModel,
              MessageType.DELETER_USER_FROM_GROUP);
      if (responseBoolean.equalsIgnoreCase("true")) {
        FrontEnd.getView().sendToView("SUCCESS: Operation Successful: \t" + userToDelete +
                " has been deleted now.");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      } else {
        FrontEnd.getView().sendToView("ERROR: Operation Failed. Please try again");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      }
    }
  }

  private boolean oneMemberCheckDeleteUser(Map<String, Boolean> userModMap) {
    if(userModMap.size() <= 1) {
      FrontEnd.getView().sendToView("ERROR: Only " + userModMap.size() + " member left in group." +
              "Can't delete users for now.\n Taking you back\n");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      return true;
    }
    return false;
  }
}
