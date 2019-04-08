package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;

import java.util.ArrayList;
import java.util.List;

public class ListFollowerUserHandler implements MessageHandler {
    private Gson gson;
    private JPAService jpaService;

    public ListFollowerUserHandler() {
        gson =  new Gson();
        jpaService = JPAService.getInstance();
    }
    @Override
    public boolean handleMessage(String user, String message, Connection clientConnection) {
        try {

            List<User> queryRes = jpaService.getAllFollowers(user);

            MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
                    MessageType.USER_SEARCH, gson.toJson(queryRes));
            sendResponse(responsePacket, clientConnection);
        } catch (Exception e) {
            ChatLogger.error(e.getMessage());
            return false;
        }
        return true;
    }
}
