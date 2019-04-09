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
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.model.RecallModel;
import edu.northeastern.ccs.im.model.TranslateModel;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UserChatModelLayer implements CoreOperation {

  private static final String QUIT = "\\q";
  private static final String REVERSE = "\\r";
  private static final String TRANSLATE = "\\t";
  private Connection connLocal;
  private Gson gson;
  private ChatModel chatModel;
  private String userToChat;
  private boolean shouldListenForMessages = true;


  @Override
  public void passControl(Scanner scanner, Connection connectionLayerModel) {
    connLocal = connectionLayerModel;
    gson = new Gson();
    FrontEnd.getView().sendToView("INFO: Chat initiated.");
    FrontEnd.getView().sendToView("INPUT: Enter Message or enter \\q to quit");
    initReaderThread();

    //Sending the server status that user is about to start the chat.
    UserChat userChatObject = new UserChat();
    MessageJson msg = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.USER_CHAT_START,
            gson.toJson(userChatObject));
    connectionLayerModel.sendMessage(msg);

    while (scanner.hasNext()) {

      String message = scanner.nextLine().trim();
      if (message.isEmpty()) {
        continue;
      }

      if (message.equalsIgnoreCase(QUIT)) {
        // Quitting the chat
        shouldListenForMessages = false;
        FrontEnd.getView().sendToView("INFO: Ending Chat.");
        breakFromConversation(connectionLayerModel);
        FrontEnd.getView().showUserLevelOptions();
        return;
      }
      else if (message.equalsIgnoreCase(REVERSE)) {
        FrontEnd.getView().sendToView("INPUT: Enter Number of messages you want to unsend.");
        FrontEnd.getView().sendToView("CAUTION: Only unseen messages can be unsend.");
        try {
          int num = Integer.parseInt(scanner.next());
          FrontEnd.getView().sendToView(String.valueOf(num));
          RecallModel recallModel = new RecallModel(userToChat, num);
          MessageJson messageJs = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.CHAT_RECALL,
                  gson.toJson(recallModel));
          connectionLayerModel.sendMessage(messageJs);

        }
        catch (NumberFormatException ex) {
          FrontEnd.getView().sendToView("ERROR: Not a number, try again by entering \\r.");
        }
      }

      else if (message.equalsIgnoreCase(TRANSLATE)) {
        FrontEnd.getView().sendToView("INPUT: Enter the language you want to translate to.");


        try {
          String language = scanner.nextLine();
          FrontEnd.getView().sendToView("INPUT: Enter the message");
          String messageConvert = scanner.nextLine();
          TranslateModel translateModel = new TranslateModel(messageConvert, language);
          translateModel.setFromUser(GenerateLoginCredentials.getUsername());
          translateModel.setToUser(this.userToChat);
          MessageJson messageTranslate = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.TRANSLATE_MESSAGE,
                  gson.toJson(translateModel));
          connectionLayerModel.sendMessage(messageTranslate);
        }
        catch (Exception ex) {
          FrontEnd.getView().sendToView("ERROR: error sending message \\r.");
        }
      }

      else {
        // Send chat to server
        chatModel.setMsg(message);
        MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.USER_CHAT,
                gson.toJson(chatModel));
        connectionLayerModel.sendMessage(messageJson);
        initReaderThread();
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
    this.userToChat = userToChat;
    chatModel = new ChatModel();
    chatModel.setFromUserName(GenerateLoginCredentials.getUsername());
    chatModel.setToUserName(userToChat);
  }


  /**
   * This method runs in a loop when thread starts till message quit comes
   */
  public void displayResponse(MessageJson response) {
    if (response == null) {
      FrontEnd.getView().sendToView("ERROR: Uh Oh! Something went wrong. Please try again");
      return;
    }

    if (response.getMessageType().equals(MessageType.USER_CHAT) ||
            response.getMessageType().equals(MessageType.GROUP_CHAT)) {

      ChatModel chat = gson.fromJson(response.getMessage(), ChatModel.class);
      String messageToDisplay = response.getMessageType().name() + "-" + response.getFromUser() + ": " + chat.getMsg();
      FrontEnd.getView().sendToView(messageToDisplay);
    }

    else if (response.getMessageType().equals(MessageType.AUTH_ACK)){

      AckModel ackModel = gson.fromJson(response.getMessage(), AckModel.class);
      String messageToDisplay = response.getMessageType().name() + " " + ackModel.getErrorMessage();
      FrontEnd.getView().sendToView(messageToDisplay);
    }
  }

}
