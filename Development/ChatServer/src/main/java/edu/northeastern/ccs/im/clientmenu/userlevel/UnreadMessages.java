package edu.northeastern.ccs.im.clientmenu.userlevel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.database.Chat;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.view.FrontEnd;
import org.jsoup.helper.StringUtil;

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
        System.out.println(resp);
        Type chatModelList = new TypeToken<List<Chat>>() {
        }.getType();
        List<Chat> listUnreadMessagesAll = new Gson().fromJson(resp, chatModelList);
        List<Chat> listChatExceptGroup = new ArrayList<>();

        FrontEnd.getView().sendToView("Unread Group Messages:");
        for (Chat eachChat : listUnreadMessagesAll) {
            // Check if it is a group message
            if (eachChat.getGrpMsg()) {
                // TODO: Have Server side create a custom Object with group and normal isolated
                FrontEnd.getView().sendToView(eachChat.getMsg());
            } else {
                listChatExceptGroup.add(eachChat);
            }
        }


        FrontEnd.getView().sendToView("Unread Chat Messages:");
        for (Chat eachChat : listChatExceptGroup){
            FrontEnd.getView().sendToView(eachChat.getMsg());
        }

    }
}
