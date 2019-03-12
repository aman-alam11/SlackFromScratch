package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import java.util.Date;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.server.Connection;

public class ChatHandler implements MessageHandler {

  private Gson mGson;

  public ChatHandler() {
    mGson = new Gson();
  }

  @Override
  public boolean handleMessage(String user, String message, Connection conn) {
    ChatModel chatModel = mGson.fromJson(message, ChatModel.class);

    JPAService jpaService = new JPAService();

    return jpaService.createChatMessage(chatModel.getSender().getId(),
            chatModel.getReciever().getId(), chatModel.getConversation(),
            0, chatModel.getTimeStamp(), new Date(),
            false, false);
  }
}
