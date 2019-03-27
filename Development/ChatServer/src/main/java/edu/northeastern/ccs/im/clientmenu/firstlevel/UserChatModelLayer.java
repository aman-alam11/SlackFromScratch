package edu.northeastern.ccs.im.clientmenu.firstlevel;

import com.google.gson.Gson;

import java.util.Scanner;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.models.UserChat;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UserChatModelLayer implements CoreOperation {

  private static final String QUIT = "\\q";
  private Connection connLocal;
  private Gson gson;
  private UserChat userChat;
  boolean shouldListenForMessages = true;


  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    connLocal = connectionLayerModel;
    gson = new Gson();
    FrontEnd.getView().sendToView("Conversation initiated.");
    FrontEnd.getView().sendToView("Enter Message or enter \\q to quit");
    initReaderThread();
    while (scanner.hasNext()) {

      String message = scanner.nextLine().trim();

      if (!message.equals(QUIT)) {

        userChat.setMsg(message);
        MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.USER_CHAT,
                new Gson().toJson(userChat));
        connectionLayerModel.sendMessage(messageJson);
        initReaderThread();
      } else {
        shouldListenForMessages = false;
        FrontEnd.getView().sendToView("Ending conversation.");
        breakFromConversation(connectionLayerModel);
        FrontEnd.getView().showUserLevelOptions();
        break;
      }
    }
  }

  private void initReaderThread() {
    if(connLocal == null){
      return;
    }

    new Thread(() -> {
      while (shouldListenForMessages) {
        try {
          Thread.sleep(500);
          if(connLocal.hasNext()){
            // TODO: Check if it is of response type
            displayResponse(connLocal.next());
          }
        } catch (Exception e) {
          ChatLogger.error("Unable to make the Thread sleep");
        }
      }
    }).start();

  }


  private void breakFromConversation(Connection connectionLayerModel) {
    userChat = new UserChat();
    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.CHAT_QUIT,
            new Gson().toJson(userChat));
    connectionLayerModel.sendMessage(messageJson);
  }


  public UserChatModelLayer(String userToChat) {
    // Create Object
    userChat = new UserChat();
    userChat.setFromUserName(GenerateLoginCredentials.getUsername());
    userChat.setToUserName(userToChat);

  }


  /**
   * This method runs in a loop when thread starts till messgage quit comes
   */
  public void displayResponse(MessageJson response) {
    if (response == null) {
      FrontEnd.getView().sendToView("Uh Oh! Something went wrong. Please try again");
      return;
    }

    if (response.getMessageType().equals(MessageType.USER_CHAT) ||
            response.getMessageType().equals(MessageType.GROUP_CHAT)) {

      UserChat chat = gson.fromJson(response.getMessage(), UserChat.class);
      String messageToDisplay = response.getMessageType().name() + "-" + chat.toString();
      FrontEnd.getView().sendToView(messageToDisplay);
    }
  }

}
