package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentGroupName;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.AddDeleteGroupUsers;
import edu.northeastern.ccs.im.model.ErrorCodes;
import edu.northeastern.ccs.im.server.business.logic.AddGroupUsersHandler;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class AddUserToGroup implements CoreOperation {

  private Gson mGson;
  private Scanner scanner;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    this.mGson = new Gson();
    this.scanner = scanner;

    FrontEnd.getView().sendToView("INPUT: Enter name of users to be added split by \",\"");
    String scr = scanner.nextLine().trim();
    String[] users = scr.split(",");

    AddDeleteGroupUsers addDeleteGroupUsers = new AddDeleteGroupUsers();
    addDeleteGroupUsers.setGroupName(CurrentGroupName.getGroupName());
    addDeleteGroupUsers.setUsersList(Arrays.asList(users));

    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
            MessageType.ADD_USER_IN_GROUP, mGson.toJson(addDeleteGroupUsers));

    connectionLayerModel.sendMessage(messageJson);


    String responseSocket = waitForResponseSocket(connectionLayerModel);
    if (!StringUtil.isBlank(responseSocket)) {
      AckModel ackModel = mGson.fromJson(responseSocket, AckModel.class);
      List<ErrorCodes> errorCodes = ackModel.getErrorCodeList();

      if (errorCodes.isEmpty()) {
        FrontEnd.getView().sendToView("SUCCESS: Users Added: ");
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_USERS_CRUD_LEVEL);

      } else {
        for (ErrorCodes error: errorCodes) {
          FrontEnd.getView().sendToView("ERROR: " + error.getErrorMessage() + "!");
          FrontEnd.getView().showGroupLevelOptions();
        }
      }
    } else {
      // TODO: Some default response
    }





  }
}
