package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.northeastern.ccs.im.model.GroupSearchModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.GroupCreationHandler;


public class GroupCreationHandlerTest {

  @Mock
  private Connection connection;

  private GroupCreationHandler groupCreationHandler;
  private Gson gson;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    groupCreationHandler = new GroupCreationHandler();
    gson = new Gson();
  }

  @Test
  public void handleMessageTest() {
    GroupSearchModel groupSearch = new GroupSearchModel("nnn");
    boolean b = groupCreationHandler.handleMessage("user", gson.toJson(groupSearch), connection);
    //assertTrue(b);
  }
}
