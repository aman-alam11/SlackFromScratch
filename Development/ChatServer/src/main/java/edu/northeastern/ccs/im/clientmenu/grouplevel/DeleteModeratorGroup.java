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

public class DeleteModeratorGroup implements CoreOperation {

  private Scanner mScanner;
  private Connection mConnectionLayerModel;


  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    mScanner = scanner;
    mConnectionLayerModel = connectionLayerModel;

    Map<String, Boolean> userMap = getAllUserGroup(connectionLayerModel);


    if (userMap == null ) {
      FrontEnd.getView().sendToView("ERROR: Operation Failed. Please try again");
    }

    else {
      parseResponse(userMap);
    }
  }

  private void parseResponse(Map<String, Boolean> userModMap) {


    FrontEnd.getView().sendToView("INPUT: Which user from above do you want to downgrade from as moderator?");
    String userToDowngrade = mScanner.nextLine().trim();
    if (!userModMap.containsKey(userToDowngrade)) {
      FrontEnd.getView().sendToView("ERROR: Illegal Name Entered. Sending you back");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
    } else {
      if (!userModMap.get(userToDowngrade)) {
        // Means s/he is already a moderator
        FrontEnd.getView().sendToView("ERROR: The selected user is not a moderator. Sending you back");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      } else {

        String responseBoolean = userToToggleModeratorOrDeleteUserGroup(userToDowngrade, mConnectionLayerModel, MessageType.TOGGLE_MODERATOR);
        if (responseBoolean.equalsIgnoreCase("true")) {
          FrontEnd.getView().sendToView("SUCCESS: Operation Successful: \t" + userToDowngrade + " is not a moderator now.");
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
        } else {
          FrontEnd.getView().sendToView("ERROR: Operation Failed. Please try again");
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
        }
      }
    }

  }
}
