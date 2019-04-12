package edu.northeastern.ccs.im.server.business.logic.handler;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UserSearch;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

public class UserSearchHandler implements MessageHandler {
	
	private Gson gson;
	private JPAService jpaService;
	
	public UserSearchHandler() {
		gson =  new Gson();
		jpaService = JPAService.getInstance();
	}

	@Override
	public boolean handleMessage(String user, String message, Connection clientConnection) {
		try {
			UserSearch userSearch = gson.fromJson(message, UserSearch.class);
			List<String> queryRes = jpaService.searchUserbyUserName(userSearch.getUsername());
			List<String> listUser = new ArrayList<>();
			
			UserSearch results = new UserSearch(userSearch.getUsername());
			results.setUsersList(listUser);
			listUser.addAll(queryRes);
			//Remove itself from search list
			listUser.remove(user);
			MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
		            MessageType.USER_SEARCH, gson.toJson(results));
		    sendResponse(responsePacket, clientConnection);
		    
		} catch (Exception e) {
			ChatLogger.error(e.getMessage());
			return false;
		}
		return true;
	}

}
