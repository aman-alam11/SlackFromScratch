package edu.northeastern.ccs.im.server.business.logic;

import java.util.List;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.server.Connection;

public class GroupChatHandler implements MessageHandler {
	
	private Gson gson;
	
	public GroupChatHandler() {
		gson = new Gson();
	}
  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {
  	ChatModel groupChat = gson.fromJson(message, ChatModel.class);
  	List<User> groupUsers = JPAService.getInstance().findAllMembersOfGroup(groupChat.getToUserName());
  	MessageHandler chatHandler= new ChatHandler();
  	for (User gUser : groupUsers) {
  		String msg = convertUserChatToGroupChat(groupChat, gUser.getName());
  		chatHandler.handleMessage(user, msg, clientConnection);
  	}
    return true;
  }
  
  private String convertUserChatToGroupChat(ChatModel chat, String receiver) {
  	ChatModel groupChat = new ChatModel();
  	groupChat.setFromUserName(chat.getFromUserName());
  	groupChat.setToUserName(receiver);
  	groupChat.setGroupName(chat.getGroupName());
  	groupChat.setMsg(chat.getMsg());
  	groupChat.setTimestamp(chat.getTimestamp());
  	return gson.toJson(groupChat);
  }
}
