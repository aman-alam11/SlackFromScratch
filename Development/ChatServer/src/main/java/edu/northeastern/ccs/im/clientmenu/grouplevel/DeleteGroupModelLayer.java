package edu.northeastern.ccs.im.clientmenu.grouplevel;

import java.util.Map;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class DeleteGroupModelLayer implements CoreOperation {
  private Scanner mScanner;
  private Connection mConnectionLayerModel;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    mScanner = scanner;
    mConnectionLayerModel = connectionLayerModel;
    FrontEnd.getView().sendToView("INFO: Let's see how many groups you belong to; " +
            "Fetching all your groups that you can modify...\n");

    parseResponse(GetAllGroupsUtil.parseResponse(MessageType.GET_ALL_GROUPS_MOD, connectionLayerModel));
  }

  private void parseResponse(Map<String, Boolean> listGroupMod) {

    if (listGroupMod == null || listGroupMod.isEmpty()) {
      FrontEnd.getView().sendToViewSameLine("ERROR: Uh Oh! Please try again, operation failed");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      return;
    }

    // Delete the group
    FrontEnd.getView().sendToViewSameLine("INPUT: Which of the above groups do you want to delete? Enter name of that group...\n");
    String getGroupToDelete = mScanner.nextLine().trim();

    if (listGroupMod.containsKey(getGroupToDelete)) {
      if (listGroupMod.get(getGroupToDelete)) {
        sendMessageToDeleteGroup(getGroupToDelete);
      } else {
        FrontEnd.getView().sendToView("ERROR: You do not have rights to delete this group. Sending you back");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      }
    } else {
      FrontEnd.getView().sendToView("Illegal Input. Sending you back");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
    }
  }

  private void sendMessageToDeleteGroup(String groupToDelete) {

    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
            MessageType.DELETE_GROUP, groupToDelete);
    mConnectionLayerModel.sendMessage(messageJson);
    String response = waitForResponseSocket(mConnectionLayerModel);
    if (response.equalsIgnoreCase("true")) {
      FrontEnd.getView().sendToView("SUCCESS: Successfully deleted group: " + groupToDelete);
    } else {
      FrontEnd.getView().sendToView("ERROR: Unable to delete group. Please try again");
    }

    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);

  }
}
