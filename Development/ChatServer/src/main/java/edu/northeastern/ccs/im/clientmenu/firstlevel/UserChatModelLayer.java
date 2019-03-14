package edu.northeastern.ccs.im.clientmenu.firstlevel;

import com.google.gson.Gson;

import java.util.Scanner;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.client.communication.AsyncListener;
import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.client.communication.SocketConnection;
import edu.northeastern.ccs.im.clientmenu.clientinterfaces.CoreOperation;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.models.UserChat;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.view.FrontEnd;

public class UserChatModelLayer implements CoreOperation, AsyncListener, Runnable {

  private String userToChatWith;
  private static final String QUIT = "\\q";
  private boolean isAlive;
  private Connection connLocal;
  private Gson gson;

  @Override
	public void passControl(Scanner scanner, Connection connectionLayerModel) {
  	connLocal = connectionLayerModel;
  	gson = new Gson();
  	isAlive = true;
  	//Start a thread to read incoming messages and display them
  	new Thread(this).start();
		FrontEnd.getView().sendToView("Enter Message, \\q to quit");
		while (scanner.hasNext()) {

			String message = scanner.nextLine().trim();

			if (!message.equals(QUIT)) {
				// Create Object
				UserChat userChat = new UserChat();
				userChat.setFromUserName(GenerateLoginCredentials.getUsername());
				userChat.setToUserName(userToChatWith);
				userChat.setMsg(message);

				// Send Object
				MessageJson messageJson = new MessageJson(GenerateLoginCredentials.getUsername(), MessageType.USER_CHAT,
						new Gson().toJson(userChat));
				connectionLayerModel.sendMessage(messageJson);
			} else {
				isAlive = false;
				break;
			}

		}
	}
  
  public UserChatModelLayer(String userToChat) {
    this.userToChatWith = userToChat;
  }
  
  @Override
  public void listen(String message) {
    FrontEnd.getView().showLoadingView(true);

  }

  /**
   * This method runs in a loop when thread starts till messgage quit comes
   */
	@Override
	public void run() {
		while (isAlive) {
			if (connLocal.hasNext()) {
				MessageJson msg = connLocal.next();
				if (msg.getMessageType().equals(MessageType.USER_CHAT) || msg.getMessageType().equals(MessageType.GROUP_CHAT)) {

					UserChat chat = gson.fromJson(msg.getMessage(), UserChat.class);
					String messageToDisplay = frameChatMessageToDisplay(chat, msg.getMessageType());
					FrontEnd.getView().sendToView(messageToDisplay);
					
				}

			}
			sleepFor(100);
		}
	}

	private void sleepFor(int milliSecs) {
		try {
			Thread.sleep(milliSecs);
		} catch (InterruptedException e) {
			ChatLogger.error(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
	
	public String frameChatMessageToDisplay(UserChat chat, MessageType msgType) {
		StringBuilder sb = new StringBuilder(msgType.name());
		sb.append("-")
			.append(chat.toString());
		return sb.toString();							
		
	}
}
