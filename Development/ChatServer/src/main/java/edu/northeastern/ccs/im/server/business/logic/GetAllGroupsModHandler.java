package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.util.Map;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;

public class GetAllGroupsModHandler implements MessageHandler {

    private static final String LOG_TAG = GetAllGroupsModHandler.class.getSimpleName();
    private JPAService mJpaService;

    public GetAllGroupsModHandler() {
        mJpaService = JPAService.getInstance();
    }


    @Override
    public boolean handleMessage(String user, String message, Connection clientConnection) {
        try {
            if(user == null || StringUtil.isBlank(user)){
                return false;
            }

            Gson gson = new Gson();

            MessageJson messageJson = gson.fromJson(message, MessageJson.class);

            if (messageJson.getMessageType().equals(MessageType.GET_ALL_GROUPS_MOD)) {

                Map<String, Boolean> listAllGroupsForUser = mJpaService.getAllGroupsForUser(user);

                MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
                        MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

                sendResponse(response, clientConnection);
                return true;
            }
            else {
                ChatLogger.info(LOG_TAG + messageJson.getMessage() + messageJson.getMessageType());
            }

        } catch (Exception e) {
            ChatLogger.error(LOG_TAG + " : " + e.getMessage());
            return false;
        }

        return false;
    }
}
