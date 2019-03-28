package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.helper.StringUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
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

        FrontEnd.getView().sendToView("\nLOADING\n");
        String resp = waitForResponseSocket(connectionModelLayer);
        if (!StringUtil.isBlank(resp)) {
            this.displayResponse(resp);
        } else {
            // TODO: Some default response
        }
    }

    private void displayResponse(String resp) {
        Type chatModelList = new TypeToken<List<UnreadMessageModel>>() {
        }.getType();
        List<UnreadMessageModel> listUnreadMessagesAll = new Gson().fromJson(resp, chatModelList);

        FrontEnd.getView().sendToView("Unread Group Messages:");
        for (UnreadMessageModel eachChat : listUnreadMessagesAll) {
            // Check if it is a group message
            if (eachChat.isGroupMessage()) {
                FrontEnd.getView().sendToView(eachChat.toString());
            }
        }

        FrontEnd.getView().sendToView("Unread Chat Messages:");
        for (UnreadMessageModel eachChat : listUnreadMessagesAll) {
            // Check if it is a group message
            if (!eachChat.isGroupMessage()) {
                FrontEnd.getView().sendToView(eachChat.toString());
            }
        }
        FrontEnd.getView().showUserLevelOptions();
    }
}