package edu.northeastern.ccs.im.server.business.logic;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.server.Connection;

public class DeleteGroupHandler implements MessageHandler {

  private static final String LOG_TAG = DeleteGroupHandler.class.getSimpleName();
  private JPAService mJpaService;

  public DeleteGroupHandler() {
    mJpaService = JPAService.getInstance();
  }


  @Override
  public boolean handleMessage(String user, String message, Connection clientConnection) {

    return false;
  }
}
