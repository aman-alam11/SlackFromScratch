package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.LoginCredentials;
import edu.northeastern.ccs.im.server.Connection;

public class UserCreationHandler implements MessageHandler {

  private Gson gson;

  public UserCreationHandler() {
    gson = new Gson();
  }


  @Override
  public boolean handleMessage(String user, String message, Connection connection) {

    LoginCredentials loginCredentials = gson.fromJson(message, LoginCredentials.class);

    SessionFactory sessionFactory = SessionFactory.getInstance(loginCredentials.getUserName(),
            loginCredentials.getPassword(), new JPAService());
    
    boolean isSuccessfull = sessionFactory.createAccount();
    
    String responseMsg = isSuccessfull ? 
    						MessageConstants.REGISTRATION_SUCCESS : 
    						MessageConstants.REGISTRATION_FAILURE;
    
    AckModel ackMessage = new AckModel(isSuccessfull, responseMsg);
    MessageJson reponsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE, 
    											MessageType.AUTH_ACK, 
    											gson.toJson(ackMessage));
    sendRespose(reponsePacket, connection);

    return isSuccessfull;
  }
}
