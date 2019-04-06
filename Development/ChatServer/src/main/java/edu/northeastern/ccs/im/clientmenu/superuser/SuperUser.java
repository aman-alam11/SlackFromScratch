package edu.northeastern.ccs.im.clientmenu.superuser;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.view.FrontEnd;

/**
 * A class for all the super user options.
 */
public class SuperUser implements CoreOperation {
  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {

    FrontEnd.getView().showSuperUserOperations();
    int choice = Integer.parseInt(scanner.nextLine());
    switch (choice) {

      // User to User chat only for a particular user
      case 1:
        
        break;

      // Group Chat for a particular group
      case 2:

        break;

        // Get all user to user chats and group chats for a particular user
      case 3:
        break;

      default:
        // send back to first level
        FrontEnd.getView().sendToView("Wrong option selected. Sending you back");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
    }
  }
}
