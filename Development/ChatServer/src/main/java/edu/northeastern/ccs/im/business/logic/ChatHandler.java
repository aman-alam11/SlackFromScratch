package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import java.util.Date;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.ChatModel;

public class ChatHandler implements MessageHandler {

  private Gson mGson;

  public ChatHandler() {
    mGson = new Gson();
  }

  @Override
  public boolean handleMessage(String user, String message) {
    ChatModel chatModel = mGson.fromJson(message, ChatModel.class);

    JPAService jpaService = new JPAService();

    return jpaService.createChatMessage(chatModel.getSender().getId(),
            chatModel.getReciever().getId(), chatModel.getConversation(),
            0, new Date(),
            false, false);
  }
}
