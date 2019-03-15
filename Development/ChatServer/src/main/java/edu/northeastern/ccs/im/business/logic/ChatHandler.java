package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UserChat;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.Prattle;

/**
 * This is the handler for chat.
 * It sends message to another user.
 */
public class ChatHandler implements MessageHandler {

  private Gson mGson;

  public ChatHandler() {
    mGson = new Gson();
  }

  @Override
  public boolean handleMessage(String user, String message, Connection conn) {
    boolean isSuccessfull = false;
    UserChat chatModel = mGson.fromJson(message, UserChat.class);
    JPAService jpaService = new JPAService();
    
    int id = jpaService.createChatMessage(chatModel.getFromUserName(),
      																							chatModel.getToUserName(),
      																							chatModel.getMsg(),
      																							0,
      																							chatModel.getExpiry(),
      																							false,
      																							false);

    if (id !=0 && Prattle.isUserOnline(chatModel.getToUserName())) {

      MessageJson msg = new MessageJson(chatModel.getFromUserName(), MessageType.USER_CHAT, message);
      msg.setSendToUser(chatModel.getToUserName());
      isSuccessfull = Prattle.sendMessageTo(chatModel.getToUserName(), msg);
      if (isSuccessfull) {
       jpaService.updateChatStatus(id, true);
      }
    }
    return isSuccessfull;
  }
}
