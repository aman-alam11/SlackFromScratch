package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UserSearch;
import edu.northeastern.ccs.im.view.FrontEnd;

import java.util.Scanner;

public class ListUserFollowerLayer implements CoreOperation {
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
    }
}
