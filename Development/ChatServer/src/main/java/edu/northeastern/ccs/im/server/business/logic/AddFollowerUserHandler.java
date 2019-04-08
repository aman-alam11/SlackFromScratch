package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UserFollow;
import edu.northeastern.ccs.im.model.UserSearch;
import edu.northeastern.ccs.im.server.Connection;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles moderator rights as well as deletion of user.
 */
public class AddFollowerUserHandler implements MessageHandler {

    private static final String LOG_TAG = AddFollowerUserHandler.class.getSimpleName();
    private Gson gson;
    private JPAService jpaService;

    public AddFollowerUserHandler() {
        gson =  new Gson();
        jpaService = JPAService.getInstance();
    }

    @Override
    public boolean handleMessage(String user, String message, Connection clientConnection) {
        boolean addedFollower;
        try {
            UserFollow userFollow = gson.fromJson(message, UserFollow.class);
            addedFollower = jpaService.addFollower(userFollow.getUserName(),userFollow.getFollowerName());
        } catch (Exception e) {
            ChatLogger.error(e.getMessage());
            return false;
        }
        return addedFollower;
    }
}