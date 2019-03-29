package edu.northeastern.ccs.im.clientmenu.grouplevel;


import java.util.Map;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.grouplevel.GetAllGroupsUtil.getAllUserGroup;
import static edu.northeastern.ccs.im.clientmenu.grouplevel.GetAllGroupsUtil.userToToggleModerator;

public class DeleteModeratorGroup implements CoreOperation {

  private Scanner mScanner;
  private Connection mConnectionLayerModel;


  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    mScanner = scanner;
    mConnectionLayerModel = connectionLayerModel;

    parseResponse(getAllUserGroup(connectionLayerModel));
  }

  private void parseResponse(Map<String, Boolean> userModMap) {


    FrontEnd.getView().sendToView("Which user from above do you want to downgrade from as moderator?");
    String userToDowngrade = mScanner.nextLine().trim();
    if (!userModMap.containsKey(userToDowngrade)) {
      FrontEnd.getView().sendToView("Illegal Name Entered. Sending you back");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
    } else {
      if (!userModMap.get(userToDowngrade)) {
        // Means s/he is already a moderator
        FrontEnd.getView().sendToView("The selected user is not a moderator. Sending you back");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      } else {

        String responseBoolean = userToToggleModerator(userToDowngrade, mConnectionLayerModel);
        if (responseBoolean.equalsIgnoreCase("true")) {
          FrontEnd.getView().sendToView("Operation Successful: \t" + userToDowngrade + " is not a moderator now.");
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
        } else {
          FrontEnd.getView().sendToView("Operation Failed. Please try again");
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
        }
      }
    }

  }
}