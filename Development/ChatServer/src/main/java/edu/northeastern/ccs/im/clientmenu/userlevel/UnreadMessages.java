package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.helper.StringUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UnreadMessageModel;
import edu.northeastern.ccs.im.view.FrontEnd;

import static edu.northeastern.ccs.im.clientmenu.clientutils.WaitForResponse.waitForResponseSocket;

public class UnreadMessages implements CoreOperation {

    @Override
    public void passControl(Scanner scanner, Connection connectionModelLayer) {

        MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.UNREAD_MSG, "");
        connectionModelLayer.sendMessage(messageJson);

        String resp = waitForResponseSocket(connectionModelLayer);
        if (!StringUtil.isBlank(resp)) {
            this.displayResponse(resp);
        } else {
            InjectLevelUtil.getInstance().injectLevel(CurrentLevel.DEFAULT_LEVEL);
        }
    }

    private void displayResponse(String resp) {
        Type chatModelList = new TypeToken<List<UnreadMessageModel>>() {
        }.getType();
        List<UnreadMessageModel> listUnreadMessagesAll = new Gson().fromJson(resp, chatModelList);


        if(!listUnreadMessagesAll.isEmpty())  {
            FrontEnd.getView().sendToView("GROUP MESSAGES: \n");
            FrontEnd.getView().enterLines();
            for (UnreadMessageModel eachChat : listUnreadMessagesAll) {
                // Check if it is a group message
                if (eachChat.isGroupMessage()) {
                    FrontEnd.getView().sendToView(eachChat.toString());
                }
            }
            FrontEnd.getView().enterLines();

            FrontEnd.getView().sendToView("CHAT MESSAGES: \n");

            FrontEnd.getView().enterLines();

            for (UnreadMessageModel eachChat : listUnreadMessagesAll) {
                // Check if it is a group message
                if (!eachChat.isGroupMessage()) {
                    FrontEnd.getView().sendToView(eachChat.toString());
                }
            }
            FrontEnd.getView().enterLines();
        }
        else {
            FrontEnd.getView().sendToView("INFO: No new messages");
        }


        FrontEnd.getView().showUserLevelOptions();
    }
}