package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.Prattle;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

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
        ChatModel chatModel = mGson.fromJson(message, ChatModel.class);
        chatModel.setDelivered(false);
        JPAService jpaService = JPAService.getInstance();

        long id = jpaService.createChatMessage(chatModel);

        if (id > 0) {
            isSuccessfull = true;
        }

        /**
         * If the user to whom the message is sent is currently online,
         * then set the receiver for the message type user chat,
         * mark that message as delivered.
         */
        if (Prattle.isUserChatting(chatModel.getToUserName())) {
        	
        		MessageType msgType = MessageType.USER_CHAT;
        		if (chatModel.getGroupName() != null && chatModel.getGroupName().length() > 0) {
        			msgType = MessageType.GROUP_CHAT;
        		}
        		//Filtering message
        		ChatModel chatModelFilter = mGson.fromJson(message, ChatModel.class);
        		String messageRaw = chatModelFilter.getMsg();
        		String messageFiltered = ProfanityFilter.getInstance().filterMessage(messageRaw);
        		chatModelFilter.setMsg(messageFiltered);

        		String newMessage = mGson.toJson(chatModelFilter, ChatModel.class);

            MessageJson msg = new MessageJson(chatModel.getFromUserName(), msgType, newMessage);
            msg.setSendToUser(chatModel.getToUserName());
            if (Prattle.sendMessageTo(chatModel.getToUserName(), msg)) {
                isSuccessfull = jpaService.updateChatStatus(id, true);
            }
        }
        return isSuccessfull;
    }
}
