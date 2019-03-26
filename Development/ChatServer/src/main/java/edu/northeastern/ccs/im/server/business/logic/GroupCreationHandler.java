package edu.northeastern.ccs.im.server.business.logic;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.Group;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.GroupCreateUpdateModel;
import edu.northeastern.ccs.im.server.Connection;

public class GroupCreationHandler implements MessageHandler {

  private Gson mGson;

  public GroupCreationHandler() {
    this.mGson = new Gson();
  }


  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {

    GroupCreateUpdateModel groupModel = mGson.fromJson(message, GroupCreateUpdateModel.class);
    if (validateGroupName(groupModel)) {
  		JPAService.getInstance().createGroup(groupModel.getGroupName(), 
  																				 groupModel.getGroupCreator(), 
  																				 groupModel.isAuthRequired());
  	}
    //jpaService.createGroup();

    return false;
  }
  
  private boolean validateGroupName(GroupCreateUpdateModel grp) {
  	String grpName = grp.getGroupName();
  	Group groupSearch = JPAService.getInstance().findGroupByName(grpName);
  	return groupSearch == null;
  }
}
