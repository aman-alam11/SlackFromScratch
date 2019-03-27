package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.Chat;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;
import org.jsoup.helper.StringUtil;

import java.util.List;

public class UnreadMessageHandler implements MessageHandler {

    private static final String LOG_TAG = UnreadMessageHandler.class.getSimpleName();

    private Gson mGson;
    private JPAService mJpaService;

    public UnreadMessageHandler() {
        mGson =  new Gson();
        mJpaService = JPAService.getInstance();
    }

    @Override
    public boolean handleMessage(String user, String message, Connection clientConnection) {
        try {
            if(user == null || StringUtil.isBlank(user)){
                return false;
            }

            List<Chat> unreadMessages = mJpaService.getUnreadMessages(user);
            MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.UNREAD_MSG, mGson.toJson(unreadMessages));
            sendResponse(response, clientConnection);
            return true;
        } catch (Exception e) {
            ChatLogger.error(LOG_TAG + " : " + e.getMessage());
            return false;
        }
    }
}
