package edu.northeastern.ccs.im.server.business.logic;

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
        JPAService jpaService = JPAService.getInstance();

        jpaService.createChatMessage(chatModel.getFromUserName(),
                chatModel.getToUserName(),
                chatModel.getMsg(),
                0,
                chatModel.getExpiry(),
                false,
                false);

        /**
         * If the user to whom the message is sent is currently online,
         * then set the receiver for the message type user chat,
         * mark that message as delivered.
         */
        if (Prattle.isUserOnline(chatModel.getToUserName())) {

            MessageJson msg = new MessageJson(chatModel.getFromUserName(), MessageType.USER_CHAT, message);
            msg.setSendToUser(chatModel.getToUserName());
            //jpaService.updateChatStatus();
            isSuccessfull = Prattle.sendMessageTo(chatModel.getToUserName(), msg);
        }
        return isSuccessfull;
    }
}
