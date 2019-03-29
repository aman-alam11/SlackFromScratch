package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentGroupName;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;
import org.jsoup.helper.StringUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class DeleteModeratorGroup implements CoreOperation {

    private Scanner mScanner;
    private Connection mConnectionLayerModel;


    @Override
    public void passControl(Scanner scanner, Connection connectionLayerModel) {
        mScanner = scanner;
        mConnectionLayerModel = connectionLayerModel;
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
            return;
        }
        parseResponse(response);
    }

    private void parseResponse(String response) {
        Type modList = new TypeToken<Map<String, Boolean>>() {
        }.getType();
        Map<String, Boolean> userModMap = new Gson().fromJson(response, modList);
        for (Map.Entry<String, Boolean> userBooleanEntry : userModMap.entrySet()) {
            String isMod = userBooleanEntry.getValue() ? "Yes" : "No";
            FrontEnd.getView().sendToView("USER: " + userBooleanEntry.getKey()
                    + " Already a moderator: " + isMod);
        }

        FrontEnd.getView().sendToView("Which user from above do you want to downgrade from as moderator?");
        String userToDowngrade = mScanner.nextLine().trim();
        if (!userModMap.containsKey(userToDowngrade)) {
            FrontEnd.getView().sendToView("Illegal Name Entered. Sending you back");
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
            return;
        } else {
            if (!userModMap.get(userToDowngrade)) {
                // Means s/he is already a moderator
                FrontEnd.getView().sendToView("The selected user is not a moderator. Sending you back");
                InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
                return;
            } else {

                List<String> listKeys = new ArrayList<>();
                listKeys.add(userToDowngrade);
                listKeys.add(CurrentGroupName.getGroupName());
                MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.TOGGLE_MODERATOR,
                        new Gson().toJson(listKeys));
                mConnectionLayerModel.sendMessage(messageJson);
                String responseBoolean = waitForResponseSocket(mConnectionLayerModel);
                if (responseBoolean.equalsIgnoreCase("true")) {
                    FrontEnd.getView().sendToView("Operation Successful: \t" + userToDowngrade + " is not a moderator now.");
                    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
                } else {
                    FrontEnd.getView().sendToView("Operation Failed. Please try again");
                    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
                }
            }
        }

    }
}
