package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.util.List;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.FetchLevel;
import edu.northeastern.ccs.im.model.UnreadMessageModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

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
			if (user == null || StringUtil.isBlank(user)) {
				return false;
			}
			List<UnreadMessageModel> unreadMessages = mJpaService.getUnreadMessages(user, null,
							FetchLevel.UNREAD_MESSAGE_HANDLER);

			for (UnreadMessageModel messageModel: unreadMessages) {
				String messageRaw = messageModel.getMessage();
				String messageFiltered = ProfanityFilter.filterMessage(messageRaw);
				messageModel.setMessage(messageFiltered);
			}


			MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.UNREAD_MSG,
					mGson.toJson(unreadMessages));
			sendResponse(response, clientConnection);
			mJpaService.setDeliveredUnreadMessages(user);
			return true;

		} catch (Exception e) {
			ChatLogger.error(LOG_TAG + " : " + e.getMessage());
			return false;
		}
	}
}
