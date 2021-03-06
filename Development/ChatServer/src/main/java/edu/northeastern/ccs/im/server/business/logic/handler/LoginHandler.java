package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.LoginCredentials;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

/**
 * This is a login handler which handles login.
 * login state true/false is returned with handleMessage method if login is successful/fail.
 */
public class LoginHandler implements MessageHandler {

  private Gson gson;
  private static final String TAG = LoginHandler.class.getSimpleName();
  private boolean isSuperUser;

  public LoginHandler() {
    gson = new Gson();
  }

  @Override
  public boolean handleMessage(String user, String message, Connection conn) {
    try {
      LoginCredentials lgn = gson.fromJson(message, LoginCredentials.class);

      SessionFactory sessionFactory = SessionFactory.getInstance(lgn.getUserName(), lgn.getPassword(),
              JPAService.getInstance());

      boolean isAuthenticated = sessionFactory.login();
      isSuperUser = JPAService.getInstance().findUserByName(lgn.getUserName()).isSuperUser();

      respond(isAuthenticated, conn);
      if (isAuthenticated) {
    	  isAuthenticated = conn.signInUser(lgn.getUserName());
      }
      return isAuthenticated;
    } catch (Exception e) {
      ChatLogger.error(TAG + " : " + e.getMessage());
      respond(false, conn);
      return false;
    }
  }

  private void respond(boolean isAuthenticated, Connection conn) {
    String strMsg = isAuthenticated ? MessageConstants.LOGIN_SUCCESS :
            MessageConstants.LOGIN_FAILURE;
    AckModel responseMessage = new AckModel(isAuthenticated, strMsg, true, isSuperUser);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            gson.toJson(responseMessage));
    sendResponse(responsePacket, conn);
  }
  

}
