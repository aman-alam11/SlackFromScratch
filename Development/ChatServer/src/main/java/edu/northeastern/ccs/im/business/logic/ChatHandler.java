package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import java.util.Date;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.Prattle;

public class ChatHandler implements MessageHandler {

  private Gson mGson;

  public ChatHandler() {
    mGson = new Gson();
  }

  @Override
  public boolean handleMessage(String user, String message, Connection conn) {
	boolean isSuccessfull = false;
    ChatModel chatModel = mGson.fromJson(message, ChatModel.class);

    JPAService jpaService = new JPAService();
    	//TODO:  save to db
		/*
		 * isSuccessfull = jpaService.createChatMessage(chatModel.getSender(),
		 * chatModel.getReciever(), chatModel.getConversation(), 0,
		 * chatModel.getTimeStamp(), new Date(), false, false);
		 */
    
    if (Prattle.isUserOnline(chatModel.getReciever())) {
    	
    	MessageJson msg = new MessageJson(chatModel.getSender(), MessageType.USER_CHAT, message);
    	msg.setSendToUser(chatModel.getReciever());
    	isSuccessfull = Prattle.sendMessageTo(chatModel.getReciever(), msg);
    	if (isSuccessfull) {
    		//TODO : set the chat id below
    		jpaService.updateChatStatus(0, true);
    	}
    }
    return isSuccessfull;
  }
}
