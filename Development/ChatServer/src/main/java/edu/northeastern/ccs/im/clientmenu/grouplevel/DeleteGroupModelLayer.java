package edu.northeastern.ccs.im.clientmenu.grouplevel;

import java.util.Map;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

public class DeleteGroupModelLayer implements CoreOperation {
  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    FrontEnd.getView().sendToView("Let's see how many groups you belong to; " +
            "Fetching all your groups that you can modify...");

    parseResponse(GetAllGroupsUtil.parseResponse(MessageType.GET_ALL_GROUPS_MOD, connectionLayerModel));
  }

  private void parseResponse(Map<String, Boolean> listGroupMod) {

    if(listGroupMod == null || listGroupMod.isEmpty()){
      FrontEnd.getView().sendToViewSameLine("Uh Oh! Please try again, operation failed");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      return;
    }

    // Delete the group
  }
}
