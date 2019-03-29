package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentGroupName;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.AddDeleteGroupUsers;
import edu.northeastern.ccs.im.model.ErrorCodes;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class AddUserToGroupModelLayer implements CoreOperation {


  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    Gson mGson = new Gson();

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
        FrontEnd.getView().sendToView("SUCCESS: Users Added: " + ackModel.getErrorMessage());
        FrontEnd.getView().showGroupUsersCrudLevelOptions();
      } else {
        for (ErrorCodes error: errorCodes) {
          FrontEnd.getView().sendToView("ERROR: " + error.getErrorMessage() + "!");
        }
        if (!ackModel.getErrorMessage().isEmpty()) {
          FrontEnd.getView().sendToView("ERROR: " + ackModel.getErrorMessage() + "!");
        }
        FrontEnd.getView().showGroupUsersCrudLevelOptions();
      }
    } else {
      // TODO: Some default response
    }

  }
}
