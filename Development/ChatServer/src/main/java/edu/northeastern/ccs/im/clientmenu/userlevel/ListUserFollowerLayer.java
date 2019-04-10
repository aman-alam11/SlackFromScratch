package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UserFollwingList;
import edu.northeastern.ccs.im.model.UserSearch;
import edu.northeastern.ccs.im.view.FrontEnd;
import org.jsoup.helper.StringUtil;

import java.util.List;
import java.util.Scanner;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class ListUserFollowerLayer implements CoreOperation {
    private Gson gson;
    private Scanner mScanner;
    private Connection model;
    private static final String DELIMITER = " --- ";
    private static final String ONLINE = DELIMITER + "Online";
    private static final String OFFLINE = DELIMITER + "Offline";

    @Override
    public void passControl(Scanner scanner, Connection connectionLayerModel) {
        gson = new Gson();
        mScanner = scanner;
        model = connectionLayerModel;

        String currentUser = gson.toJson(GenerateLoginCredentials.getUsername());

        MessageJson messageJson = new MessageJson(currentUser,
                MessageType.LIST_FOLLOWERS, currentUser);

        model.sendMessage(messageJson);

        String resp = waitForResponseSocket(model);
        
        if (!StringUtil.isBlank(resp)) {
        		UserFollwingList searchResults = gson.fromJson(resp, UserFollwingList.class);
            if (searchResults == null || searchResults.getUserList().isEmpty()) {
                FrontEnd.getView().sendToView("ERROR: You have no followers.");
            } else {
                FrontEnd.getView().sendToView("RESULTS: Following are your followers.");
                int counter = 0 ;
                for (edu.northeastern.ccs.im.model.User user : searchResults.getUserList()) {
                    FrontEnd.getView().sendToView(++counter + ": " + 
                    															user.getUserName() + 
                    															(user.isOnline() ? ONLINE : OFFLINE));
                }
                FrontEnd.getView().sendToView("===================");
            }
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.FOLLOW_USER_LEVEL);
        } else {
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.DEFAULT_LEVEL);
        }
    }
}
