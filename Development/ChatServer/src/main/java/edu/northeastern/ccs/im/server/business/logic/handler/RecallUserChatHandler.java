package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.gson.Gson;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.ErrorCodes;
import edu.northeastern.ccs.im.model.RecallModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

public class RecallUserChatHandler implements MessageHandler {

  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {

    Gson mGson = new Gson();
    AckModel ackModel = new AckModel();
    RecallModel recallModel = mGson.fromJson(message, RecallModel.class);

    JPAService jpaService = JPAService.getInstance();


    int rowsAffected  = jpaService.setRollBackMessages(recallModel.getToUser(), user, recallModel.getNum());
    ackModel.appendErrorMessage("Messages recalled: " + rowsAffected);
    if (rowsAffected == 0) {
      ackModel.addErrorCode(ErrorCodes.DB001);
    }

    MessageJson messageJson = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK, mGson.toJson(ackModel));
    sendResponse(messageJson,clientConnection);
    return rowsAffected > 0;
  }
}
