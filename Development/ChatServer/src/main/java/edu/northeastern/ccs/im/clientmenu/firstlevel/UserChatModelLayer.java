package edu.northeastern.ccs.im.clientmenu.firstlevel;

import com.google.gson.Gson;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.AsyncListener;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.client.communication.SocketConnection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.models.UserChat;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UserChatModelLayer implements CoreOperation, AsyncListener {

  private String userToChatWith;

  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {

    FrontEnd.getView().sendToView("Enter Message");
    String message = scanner.nextLine().trim();

    // Create Object
    UserChat userChat = new UserChat();
    userChat.setFromUserName(GenerateLoginCredentials.getUsername());
    userChat.setToUserName(userToChatWith);
    userChat.setMsg(message);

    // Send Object
    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
            MessageType.USER_CHAT, new Gson().toJson(userChat));
    connectionLayerModel.registerListener(this, MessageType.AUTH_ACK);
    connectionLayerModel.sendMessage(messageJson);
    FrontEnd.getView().showLoadingView(false);
  }


  public UserChatModelLayer(String userToChat) {
    this.userToChatWith = userToChat;
  }


  @Override
  public void listen(String message) {
    FrontEnd.getView().showLoadingView(true);

  }
}
