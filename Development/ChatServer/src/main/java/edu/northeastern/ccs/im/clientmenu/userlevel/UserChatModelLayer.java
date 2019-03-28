package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;

import java.util.Scanner;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.models.UserChat;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UserChatModelLayer implements CoreOperation {

  private static final String QUIT = "\\q";
  private Connection connLocal;
  private Gson gson;
  private ChatModel chatModel;
  private boolean shouldListenForMessages = true;


  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    connLocal = connectionLayerModel;
    gson = new Gson();
    FrontEnd.getView().sendToView("MESSAGE: Chat initiated.");
    FrontEnd.getView().sendToView("INPUT: Enter Message or enter \\q to quit");
    initReaderThread();

    //Sending the server status that user is about to start the chat.
    UserChat userChatObject = new UserChat();
    MessageJson msg = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.USER_CHAT_START,
            gson.toJson(userChatObject));
    connectionLayerModel.sendMessage(msg);

    while (scanner.hasNext()) {

      String message = scanner.nextLine().trim();

      if (!message.equals(QUIT)) {
        chatModel.setMsg(message);
        MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.USER_CHAT,
                gson.toJson(chatModel));
        connectionLayerModel.sendMessage(messageJson);
        initReaderThread();
      } else {
        shouldListenForMessages = false;
        FrontEnd.getView().sendToView("MESSAGE: Ending Chat.");
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
    chatModel = new ChatModel();
    MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(),
            MessageType.USER_CHAT_END, gson.toJson(chatModel));
    connectionLayerModel.sendMessage(messageJson);
  }


  public UserChatModelLayer(String userToChat) {
    // Create Object
    chatModel = new ChatModel();
    chatModel.setFromUserName(GenerateLoginCredentials.getUsername());
    chatModel.setToUserName(userToChat);
  }


  /**
   * This method runs in a loop when thread starts till messgage quit comes
   */
  public void displayResponse(MessageJson response) {
    if (response == null) {
      FrontEnd.getView().sendToView("ERROR: Uh Oh! Something went wrong. Please try again");
      return;
    }

    if (response.getMessageType().equals(MessageType.USER_CHAT) ||
            response.getMessageType().equals(MessageType.GROUP_CHAT)) {

      ChatModel chat = gson.fromJson(response.getMessage(), ChatModel.class);
      String messageToDisplay = response.getMessageType().name() + "-" + chat.getMsg();
      FrontEnd.getView().sendToView(messageToDisplay);
    }
  }

}
