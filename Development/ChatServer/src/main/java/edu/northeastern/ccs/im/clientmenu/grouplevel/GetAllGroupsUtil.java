package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.helper.StringUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentGroupName;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class GetAllGroupsUtil {


  public static Map<String, Boolean> parseResponse(MessageType messageType, Connection connectionLayerModel) {
    MessageJson messageJsonState = new MessageJson(GenerateLoginCredentials.getUsername(), messageType, "");
    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), messageType,
            new Gson().toJson(messageJsonState));

    connectionLayerModel.sendMessage(messageJson);

    String response = waitForResponseSocket(connectionLayerModel);
    if (StringUtil.isBlank(response)) {
      FrontEnd.getView().sendToView("Uh Oh! Unable to process request, Let's try again!");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      return null;
    }

    Type modList = new TypeToken<Map<String, Boolean>>() {}.getType();

    Map<String, Boolean> listGroupMod = new Gson().fromJson(response, modList);
    if(listGroupMod.size() <= 0){
      FrontEnd.getView().sendToView("You are part of no groups. Sending you back.\n");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
      return listGroupMod;
    }

    FrontEnd.getView().sendToView("You are part of following groups: ");

    for (Map.Entry<String, Boolean> eachGroupAndModPair : listGroupMod.entrySet()) {
      String canModify = eachGroupAndModPair.getValue() ? "YES" : "NO";
      FrontEnd.getView().sendToView("GROUP NAME:\t" + eachGroupAndModPair.getKey()
              + " \tCAN MODIFY THIS: \t" + canModify);
    }
    return listGroupMod;
  }


  public static Map<String, Boolean> getAllUserGroup(Connection connectionLayerModel) {
    FrontEnd.getView().sendToView("Getting all users for group: " + CurrentGroupName.getGroupName());

    MessageJson messageJsonState = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.GET_ALL_USERS_FOR_GRP,
            CurrentGroupName.getGroupName());
    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.GET_ALL_USERS_FOR_GRP,
            new Gson().toJson(messageJsonState));

    connectionLayerModel.sendMessage(messageJson);

    String response = waitForResponseSocket(connectionLayerModel);
    if (StringUtil.isBlank(response)) {
      FrontEnd.getView().sendToView("Uh Oh! Unable to get all users for the group, Let's try again!");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_USERS_MODERATOR_OPERATIONS);
      return null;
    }

    Type modList = new TypeToken<Map<String, Boolean>>() {}.getType();
    Map<String, Boolean> userModMap = new Gson().fromJson(response, modList);
    for (Map.Entry<String, Boolean> userBooleanEntry : userModMap.entrySet()) {
      String isMod = userBooleanEntry.getValue() ? "Yes" : "No";
      FrontEnd.getView().sendToView("USER: " + userBooleanEntry.getKey()
              + " Already a moderator: " + isMod);
    }

    return userModMap;
  }


  public static String userToToggleModerator(String userToUpgrade, Connection mConnectionLayerModel) {
    List<String> listKeys = new ArrayList<>();
    listKeys.add(userToUpgrade);
    listKeys.add(CurrentGroupName.getGroupName());
    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.TOGGLE_MODERATOR,
            new Gson().toJson(listKeys));
    mConnectionLayerModel.sendMessage(messageJson);
    return waitForResponseSocket(mConnectionLayerModel);
  }

}
