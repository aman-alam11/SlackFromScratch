package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UserFollow;
import edu.northeastern.ccs.im.model.UserSearch;
import edu.northeastern.ccs.im.view.FrontEnd;

import java.util.List;
import java.util.Scanner;

public class CreateUserFollowLayer implements CoreOperation {

    private Gson gson;
    private Scanner mScanner;
    private Connection model;

    @Override
    public void passControl(Scanner scanner, Connection connectionLayerModel) {
        gson = new Gson();
        mScanner = scanner;
        model = connectionLayerModel;
        FrontEnd.getView().sendToView("INPUT: Enter User name you want to follow: ");
        String userName = scanner.nextLine().trim();

        UserSearch search = new UserSearch(userName);

        String userSearchJsonString = gson.toJson(search);

        MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
                MessageType.USER_SEARCH, userSearchJsonString);

        model.sendMessage(messageJson);
        parseResponse(WaitForResponse.waitForResponseSocket(model));
    }

    private void parseResponse(String userNameList) {
        UserSearch searchResults = gson.fromJson(userNameList, UserSearch.class);
        List<String> listAllUsers = searchResults.getListUserString();
        listAllUsers.remove(GenerateLoginCredentials.getUsername());
        if (listAllUsers.isEmpty()) {
            FrontEnd.getView().sendToView("No Users Found");
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.FOLLOW_USER_LEVEL);
        } else {
            handleUserResponsevalidString(listAllUsers);
        }
    }

    private void handleUserResponsevalidString(List<String> userNameList) {

        boolean success = false;

        for (int i = 0; i < userNameList.size(); i++) {
        	FrontEnd.getView().sendToView(userNameList.get(i));
        }

        FrontEnd.getView().sendToView("INPUT: Which user from above do you want to follow?");
        String userNameToFollow = mScanner.nextLine().trim();

        if(userNameList.contains(userNameToFollow)){
            UserFollow userFollow = new UserFollow(userNameToFollow, GenerateLoginCredentials.getUsername());

            MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
                    MessageType.FOLLOW_USER, gson.toJson(userFollow));
            success = model.sendMessage(messageJson);
            if (success) {
            	 FrontEnd.getView().sendToView("You are now a follower of " + userNameToFollow);
            } else {
            	FrontEnd.getView().sendToView("Oops! Something bad happened while adding you as a follower of " + userNameToFollow);
            }
        }else{
            FrontEnd.getView().sendToView("ERROR: Illegal Name Entered. Sending you back");
        }
        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.FOLLOW_USER_LEVEL);
    }
}
