package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.northeastern.ccs.im.clientmenu.models.DeleteUserFromGroupModel;
import org.jsoup.helper.StringUtil;

import java.lang.reflect.Type;
import java.util.List;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;

/**
 * This class handles moderator rights as well as deletion of user.
 */
public class ToggleModeratorRightsHandler implements MessageHandler {

    private static final String LOG_TAG = ToggleModeratorRightsHandler.class.getSimpleName();
    private JPAService mJpaService;

    public ToggleModeratorRightsHandler() {
        mJpaService = JPAService.getInstance();
    }


    @Override
    public boolean handleMessage(String user, String message, Connection clientConnection) {
        try {
            if (user == null || StringUtil.isBlank(user)) {
                return false;
            }
            boolean isSuccess = false;
            MessageJson response = null;

            Gson gson = new Gson();
            DeleteUserFromGroupModel model = gson.fromJson(message, DeleteUserFromGroupModel.class);
            Type nameList = new TypeToken<List<String>>() {}.getType();
            List<String> lst = new Gson().fromJson(model.getMessage(), nameList);

            if(model.getMessageType() == MessageType.TOGGLE_MODERATOR) {
                isSuccess = mJpaService.toggleAdminRights(lst.get(0), lst.get(1));

                response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
                        MessageType.TOGGLE_MODERATOR, gson.toJson(isSuccess));
            } else if(model.getMessageType() == MessageType.DELETER_USER_FROM_GROUP) {
                isSuccess = mJpaService.deleteMemberFromGroup(lst.get(1), lst.get(0));

                response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
                        MessageType.DELETER_USER_FROM_GROUP, gson.toJson(isSuccess));
            }

            sendResponse(response, clientConnection);
            return true;

        } catch (Exception e) {
            ChatLogger.error(LOG_TAG + " : " + e.getMessage());
            return false;
        }
    }
}
