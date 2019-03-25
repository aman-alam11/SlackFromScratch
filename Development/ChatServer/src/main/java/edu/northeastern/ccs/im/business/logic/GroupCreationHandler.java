package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.GroupModel;
import edu.northeastern.ccs.im.server.Connection;

public class GroupCreationHandler implements MessageHandler {

  private Gson mGson;

  public GroupCreationHandler() {
    this.mGson = new Gson();
  }


  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {

    GroupModel groupModel = mGson.fromJson(message, GroupModel.class);
    JPAService jpaService = JPAService.getInstance();
    //jpaService.createGroup();

    return false;
  }
}
