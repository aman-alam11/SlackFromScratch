package edu.northeastern.ccs.im.clientmenu.firstlevel;

import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CommonOperations;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UserChat extends CommonOperations {

  private Scanner mScanner;

  @Override
  public void passControl(Scanner scanner, Connection model) {
    this.mScanner = scanner;

    FrontEnd.getView().sendToView("Enter Username of user you want to chat");
    String chatUserOtherEnd = scanner.nextLine().trim();

    FrontEnd.getView().sendToView("Enter Message");
    String message = scanner.nextLine().trim();

    //TODO this is not working and we need current singed user name.
    MessageJson messageJson = new MessageJson("atti", MessageType.USER_CHAT, message);
    messageJson.setSendToUser(chatUserOtherEnd);
    model.sendMessage(messageJson);
  }
}
