package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

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

public class UserSearchModelLayer extends CommonOperations implements AsyncListener {

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
		edu.northeastern.ccs.im.clientmenu.models.UserSearch userSearch = new edu.northeastern.ccs.im.clientmenu.models.UserSearch(
				chatUserOtherEnd);

		String userSearchJsonString = new Gson().toJson(userSearch);
		MessageJson messageJson = new MessageJson(myUsername, MessageType.USER_SEARCH, userSearchJsonString);
		model.sendMessage(messageJson);

		MessageJson mjson = null;

		do {
			mjson = mConnection.next();
		} while (mjson == null || !mjson.getMessageType().equals(MessageType.USER_SEARCH));

		String json = mjson.getMessage();

		UserSearch userSearchResults = mGson.fromJson(json, UserSearch.class);
		List<String> usernames = userSearchResults.getListUserString();
		if (usernames.isEmpty()) {
			FrontEnd.getView().sendToView("No users with that name found");
		} else {
			for (String username : usernames) {
				FrontEnd.getView().sendToView(username);
			}
		}

		String userToChatWith = mScanner.nextLine();

		if (usernames.contains(userToChatWith)) {
			new UserChatModelLayer(userToChatWith).passControl(mScanner, this.mConnection);
		} else {
			FrontEnd.getView().sendToView("Invalid username");
			InjectLevelUtil.getInstance().injectLevel(CurrentLevel.LEVEL1);
		}

	}


  @Override
  public void listen(String message) {
//    FrontEnd.getView().showLoadingView(true);
    FrontEnd.getView().showLoadingView(true);
    UserSearch userSearch = mGson.fromJson(message, UserSearch.class);
    List<String> usernames = userSearch.getListUserString();
    if (usernames.isEmpty()) {
      FrontEnd.getView().sendToView("No users with that name found");
    } else {
      for (String username : usernames) {
        FrontEnd.getView().sendToView(username);
      }
    }

    String userToChatWith = this.mScanner.next().toLowerCase().trim();
    UserChatModelLayer model = new UserChatModelLayer(userToChatWith);
    model.passControl(mScanner, this.mConnection);

  }
}
