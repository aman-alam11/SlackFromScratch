package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UserFollwingList;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.Prattle;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class handles listing of the followers for a logged in user.
 */
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
			List<edu.northeastern.ccs.im.model.User> userListToReturn = new ArrayList<>();
			
			queryRes.forEach(u-> {
				edu.northeastern.ccs.im.model.User userToAddToList = new edu.northeastern.ccs.im.model.User(u.getName(), Prattle.isUserChatting(u.getName()));
				userListToReturn.add(userToAddToList);
			});
			
			
			UserFollwingList userFollowingList = new UserFollwingList(userListToReturn);
			MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.LIST_FOLLOWERS,
					gson.toJson(userFollowingList));
			sendResponse(responsePacket, clientConnection);
		} catch (Exception e) {
			ChatLogger.error(e.getMessage());
			return false;
		}
		return true;
	}
}
