package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.UserFollow;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;


/**
 * This class handles making a user follow another user.
 */
public class AddFollowerUserHandler implements MessageHandler {

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