package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

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
import edu.northeastern.ccs.im.model.ErrorCodes;
import edu.northeastern.ccs.im.model.GroupCreateUpdateModel;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class CreateGroup implements CoreOperation {

  private Gson gson;
  private Scanner scanner;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    this.scanner = scanner;
    this.gson = new Gson();
    FrontEnd.getView().sendToView("INPUT: Enter Group name you want to create: ");
    String groupName = scanner.nextLine();

    FrontEnd.getView().sendToView("INPUT: Press Y/N if you want moderators");
    boolean yesNoModerator = false;

    switch (scanner.nextLine().trim().toLowerCase()) {
        case "y":
          yesNoModerator = true;
          break;
        case "n":
          break;
        default:

          FrontEnd.getView().sendToView("ERROR: Invalid Option!");
          break;
      }

    GroupCreateUpdateModel groupCreateUpdateModel = new GroupCreateUpdateModel(groupName,
            GenerateLoginCredentials.getUsername(),yesNoModerator);

    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
            MessageType.CREATE_GROUP, gson.toJson(groupCreateUpdateModel));

    connectionLayerModel.sendMessage(messageJson);
    checkResponse(connectionLayerModel, groupName);
  }


  private void checkResponse(Connection connectionLayerModel, String groupName) {
    String responseSocket = waitForResponseSocket(connectionLayerModel);
    if (!StringUtil.isBlank(responseSocket)) {
      AckModel ackModel = gson.fromJson(responseSocket, AckModel.class);
      List<ErrorCodes> errorCodes = ackModel.getErrorCodeList();

      if (errorCodes.isEmpty()) {
        CurrentGroupName.setGroupName(groupName);
        FrontEnd.getView().sendToView("SUCCESS: Group Added: " + CurrentGroupName.getGroupName());
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
