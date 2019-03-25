package edu.northeastern.ccs.im.clientmenu.firstlevel;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.AsyncListener;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.clientmenu.models.UserSearch;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class UserSearchModelLayer extends CommonOperations {

  private Scanner mScanner;
  private Gson mGson;
  private Connection mConnection;

  @Override
  public void passControl(Scanner scanner, Connection model) {
    this.mScanner = scanner;
    this.mConnection = model;
    mGson = new Gson();

    FrontEnd.getView().sendToView("Enter Username of user you want to chat");
    String chatUserOtherEnd = scanner.nextLine().toLowerCase().trim();

    String myUsername = GenerateLoginCredentials.getUsername();
    UserSearch userSearch = new UserSearch(chatUserOtherEnd);

    String userSearchJsonString = new Gson().toJson(userSearch);
    MessageJson messageJson = new MessageJson(myUsername, MessageType.USER_SEARCH, userSearchJsonString);
    model.sendMessage(messageJson);

    List<String> usernames = null;

    String resp = waitForResponseSocket(model);
    if (!StringUtil.isBlank(resp)) {
      UserSearch userSearchResults = mGson.fromJson(resp, UserSearch.class);
      usernames = userSearchResults.getListUserString();

      if (usernames.isEmpty()) {
        FrontEnd.getView().sendToView("No users with that name found");
      } else {
        for (String username : usernames) {
          FrontEnd.getView().sendToView(username);
        }
      }

    } else {
      // TODO: Some default response
    }

    FrontEnd.getView().sendToView("Choose one of the user names from above\n");
    String userToChatWith = mScanner.nextLine();

    if (usernames!= null && usernames.contains(userToChatWith)) {
      new UserChatModelLayer(userToChatWith).passControl(mScanner, this.mConnection);
    } else {
      FrontEnd.getView().sendToView("Invalid username");
      InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);
    }

  }
}
