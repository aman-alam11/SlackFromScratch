package edu.northeastern.ccs.im.clientmenu.grouplevel;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

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
import edu.northeastern.ccs.im.model.UnreadMessageModel;
import edu.northeastern.ccs.im.view.FrontEnd;
import javafx.util.Pair;
import org.jsoup.helper.StringUtil;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class UpdateGroupModelLayer implements CoreOperation {
    private Scanner mScanner;

    @Override
    public void passControl(Scanner scanner, Connection connectionLayerModel) {
        this.mScanner = scanner;

        FrontEnd.getView().sendToView("Let's see how many groups you belong to; " +
                "Fetching all your groups, please wait");

        MessageJson messageJsonState = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.GET_ALL_GROUPS_MOD, "");
        MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.GET_ALL_GROUPS_MOD,
                new Gson().toJson(messageJsonState));

        connectionLayerModel.sendMessage(messageJson);

        String response = waitForResponseSocket(connectionLayerModel);
        if (StringUtil.isBlank(response)) {
            FrontEnd.getView().sendToView("Uh Oh! Unable to get groups, Let's try again!");
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
            return;
        }
        parseResponse(response);
    }


    private void parseResponse(String response) {
        Type modList = new TypeToken<List<Pair<String, Boolean>>>() {
        }.getType();
        List<Pair<String, Boolean>> listGroupMod = new Gson().fromJson(response, modList);
        if(listGroupMod.size() <= 0){
            FrontEnd.getView().sendToView("You are part of no groups. Sending you back.\n");
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
            return;
        }

        FrontEnd.getView().sendToView("You are part of following groups: ");

        for (Pair<String, Boolean> eachGroupAndModPair : listGroupMod) {
            String canModify = eachGroupAndModPair.getValue() ? "YES" : "NO";
            FrontEnd.getView().sendToView("GROUP NAME:\t" + eachGroupAndModPair.getKey()
                    + " CAN MODIFY THIS: \t" + canModify);
        }

        modifyGroup(listGroupMod);

    }

    private void modifyGroup(List<Pair<String, Boolean>> listGroupMod) {
        FrontEnd.getView().sendToView("Enter name of the group you want to modify from above:");
        String groupName = mScanner.nextLine().trim();
        boolean noGroupMatched = true;

        for (Pair<String, Boolean> eachGroupAndModPair : listGroupMod) {
            if(eachGroupAndModPair.getKey().equals(groupName)){
                noGroupMatched = false;
                // If current user is moderator
                if(eachGroupAndModPair.getValue()){
                    FrontEnd.getView().sendToView("Press 1 to add/delete users and 2 to add/delete moderators?");
                    String addDeleteChoice = mScanner.nextLine().trim();
                    int choice = Integer.parseInt(addDeleteChoice);
                    if(choice == 1) {
                        CurrentGroupName.setGroupName(eachGroupAndModPair.getKey());
                        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_USERS_CRUD_LEVEL);
                    } else if(choice == 2){
                        CurrentGroupName.setGroupName(eachGroupAndModPair.getKey());
                         InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_USERS_MODERATOR_OPERATIONS);
                    } else {
                        FrontEnd.getView().sendToView("Illegal Input entered. Sending you back.");
                        InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
                    }
                } else {
                    FrontEnd.getView().sendToView("You do not have rights to modify that group. Sending you back");
                    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
                }
            }
        }

        if(noGroupMatched) {
            FrontEnd.getView().sendToView("Illegal Group Name entered.");
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
        }
    }

}
