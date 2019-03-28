package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;
import javafx.util.Pair;
import org.jsoup.helper.StringUtil;

import java.lang.reflect.Type;

public class ToggleModeratorRightsHandler implements MessageHandler {

    private static final String LOG_TAG = UnreadMessageHandler.class.getSimpleName();
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

            Gson gson = new Gson();

            Type modList = new TypeToken<Pair<String, String>>() {
            }.getType();
            Pair<String, String> pairUsernameGroupName = gson.fromJson(message, modList);

            boolean isSuccess = mJpaService.toggleAdminRights(pairUsernameGroupName);

            MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
                    MessageType.TOGGLE_MODERATOR, gson.toJson(isSuccess));

            sendResponse(response, clientConnection);
            return true;

        } catch (Exception e) {
            ChatLogger.error(LOG_TAG + " : " + e.getMessage());
            return false;
        }
    }
}
