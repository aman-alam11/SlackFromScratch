package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UserSearch;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class UserSearchModelLayer extends CommonOperations {

  @Override
  public void passControl(Scanner scanner, Connection model) {

    Gson mGson = new Gson();

    FrontEnd.getView().sendToView("SEARCH: Enter Username of user you want to chat");
    String chatUserOtherEnd = scanner.nextLine().toLowerCase().trim();

    String myUsername = GenerateLoginCredentials.getUsername();
    UserSearch search = new UserSearch(chatUserOtherEnd);

    String userSearchJsonString = mGson.toJson(search);
    MessageJson messageJson = new MessageJson(myUsername, MessageType.USER_SEARCH, userSearchJsonString);
    model.sendMessage(messageJson);

    List<String> usernames = null;

    String resp = waitForResponseSocket(model);
    if (!StringUtil.isBlank(resp)) {
      UserSearch searchResults = mGson.fromJson(resp, UserSearch.class);
      usernames = searchResults.getListUserString();

      if (usernames == null || usernames.isEmpty()) {
        FrontEnd.getView().sendToView("ERROR: No users with that name found");
      } else {
        FrontEnd.getView().sendToView("RESULTS: Users found with name " + chatUserOtherEnd);
         int count = 1;
        for (String username : usernames) {
          FrontEnd.getView().sendToView(count + ": " + username);
          count++;
        }

        FrontEnd.getView().sendToView("INPUT: Enter one of the user names from above\n");
        String userToChatWith = scanner.nextLine();

        if (usernames.contains(userToChatWith)) {
          new UserChatModelLayer(userToChatWith).passControl(scanner, model);
        } else {
          FrontEnd.getView().sendToView("ERROR: Invalid username");
          InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
        }
      }

    } else {
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.DEFAULT_LEVEL);
    }
  }
}
