package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UserChatModelLayer implements CoreOperation {

  private String userToChat;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {

    FrontEnd.getView().sendToView("Enter Message");
    String message = scanner.nextLine().trim();

    // Create Object


    // Send Object

//    User userSender = new User("atti", "123", "email");
//    User userReceiver = new User(chatUserOtherEnd, "123", "email");
//
//    ChatModel chatModel = new ChatModel("atti", "atti2", message, new Date(), false);
//    String jsonChatModel = mGson.toJson(chatModel);
//
//    MessageJson messageJson = new MessageJson("atti", MessageType.USER_CHAT, jsonChatModel);
//    messageJson.setSendToUser(chatUserOtherEnd);
//    connectionLayerModel.sendMessage(messageJson);
  }


  public UserChatModelLayer(String userToChat) {
    this.userToChat = userToChat;
  }
}
