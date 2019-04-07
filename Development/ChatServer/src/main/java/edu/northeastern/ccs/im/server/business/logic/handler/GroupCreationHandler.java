package edu.northeastern.ccs.im.server.business.logic.handler;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.Group;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.ErrorCodes;
import edu.northeastern.ccs.im.model.GroupCreateUpdateModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;

public class GroupCreationHandler implements MessageHandler {

  private Gson mGson;
  private AckModel ackModel;
  public GroupCreationHandler() {
    this.mGson = new Gson();
    ackModel = new AckModel();
  }


  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {

    GroupCreateUpdateModel groupModel = mGson.fromJson(message, GroupCreateUpdateModel.class);
    boolean isSuccessFull;
    if (validateGroupName(groupModel)) {
  		isSuccessFull = JPAService.getInstance().createGroup(groupModel.getGroupName(), 
  																				 groupModel.getGroupCreator(), 
  																				 groupModel.isAuthRequired());
  		if (!isSuccessFull) {
  			//DB error while creating group
  			ackModel.addErrorCode(ErrorCodes.DB000);
  		}
  	} else {
  		//Group name already exists
  		ackModel.addErrorCode(ErrorCodes.G801);
  		isSuccessFull = false;
  	}
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, mGson.toJson(ackModel));
    sendResponse(response, clientConnection);
    return isSuccessFull;
  }
  
  private boolean validateGroupName(GroupCreateUpdateModel grp) {
  	String grpName = grp.getGroupName();
  	Group groupSearch = JPAService.getInstance().findGroupByName(grpName);
  	return groupSearch == null;
  }
}
