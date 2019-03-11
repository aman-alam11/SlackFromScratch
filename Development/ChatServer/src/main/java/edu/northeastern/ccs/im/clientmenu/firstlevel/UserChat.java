package edu.northeastern.ccs.im.clientmenu.firstlevel;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UserChat extends CommonOperations {

  private Scanner mScanner;
  private Gson mGson;

  @Override
  public void passControl(Scanner scanner, Connection model) {
    this.mScanner = scanner;
    mGson = new Gson();

    FrontEnd.getView().sendToView("Enter Username of user you want to chat");
    String chatUserOtherEnd = scanner.nextLine().trim();

    FrontEnd.getView().sendToView("Enter Message");
    String message = scanner.nextLine().trim();

    //TODO we need current singed user name.

    User userSender = new User("atti","123", "email");
    User userReceiver = new User(chatUserOtherEnd,"123", "email");

    ChatModel chatModel = new ChatModel(userSender, userReceiver,message , new Date(), false);
    String jsonChatModel =  mGson.toJson(chatModel);

    MessageJson messageJson = new MessageJson("atti", MessageType.USER_CHAT, jsonChatModel);
    messageJson.setSendToUser(chatUserOtherEnd);
    model.sendMessage(messageJson);
  }
}
