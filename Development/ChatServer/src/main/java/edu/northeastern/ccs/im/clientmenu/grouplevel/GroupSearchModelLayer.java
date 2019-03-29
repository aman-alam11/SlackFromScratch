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
import edu.northeastern.ccs.im.model.GroupSearchModel;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class GroupSearchModelLayer implements CoreOperation {

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {

    Gson mGson = new Gson();

    FrontEnd.getView().sendToView("SEARCH: Enter Group you want to chat in");
    String chatOtherGroup = scanner.nextLine().toLowerCase().trim();

    String myUsername = GenerateLoginCredentials.getUsername();
    GroupSearchModel search = new GroupSearchModel(chatOtherGroup);

    String groupSearchJsonString = mGson.toJson(search);
    MessageJson messageJson = new MessageJson(myUsername, MessageType.GROUP_SEARCH, groupSearchJsonString);
    connectionLayerModel.sendMessage(messageJson);

    List<String> groupNames = null;

    String resp = waitForResponseSocket(connectionLayerModel);
    if (!StringUtil.isBlank(resp)) {
      GroupSearchModel searchResults = mGson.fromJson(resp, GroupSearchModel.class);
      groupNames = searchResults.getListGroupString();

      if (groupNames == null || groupNames.isEmpty()) {
        FrontEnd.getView().sendToView("ERROR: No Groups with that name found");
      } else {
        for (String name : groupNames) {
          FrontEnd.getView().sendToView(name);
        }

        FrontEnd.getView().sendToView("INPUT: Enter one of the Group names from above\n");
        String groupToChatWith = scanner.nextLine();


        if (groupNames.contains(groupToChatWith)) {
          CurrentGroupName.setGroupName(groupToChatWith);
          new GroupChatModelLayer(groupToChatWith).passControl(scanner, connectionLayerModel);
        } else {
          FrontEnd.getView().sendToView("ERROR: Invalid Group name");
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
        }
      }

    } else {
      // TODO: Some default response
    }
  }
}
