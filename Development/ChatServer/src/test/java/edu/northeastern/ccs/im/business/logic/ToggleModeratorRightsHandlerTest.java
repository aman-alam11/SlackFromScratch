package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;

import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentGroupName;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.clientmenu.models.DeleteUserFromGroupModel;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.ToggleModeratorRightsHandler;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

public class ToggleModeratorRightsHandlerTest {

  @Mock
  private Connection connection;
  private ToggleModeratorRightsHandler toggleModeratorRightsHandler;

  @Mock
  private JPAService jpaService;



  @Before
  public void init() {

    MockitoAnnotations.initMocks(this);
    JPAService.setJPAService(jpaService);
    toggleModeratorRightsHandler = new ToggleModeratorRightsHandler();
  }

  @Test
  public void handleMessageTest() {
    DeleteUserFromGroupModel deleteUserFromGroupModel = new DeleteUserFromGroupModel();
    deleteUserFromGroupModel.setMessageType(MessageType.TOGGLE_MODERATOR);
    deleteUserFromGroupModel.setMessage("hello");

    when(JPAService.getInstance().toggleAdminRights(anyString(),anyString())).thenReturn(true);
    boolean flag = toggleModeratorRightsHandler.handleMessage("user", new Gson().toJson(deleteUserFromGroupModel),connection);
    assertFalse(flag);
  }

  @Test
  public void handleMessageDeleteUserTest() {
    DeleteUserFromGroupModel deleteUserFromGroupModel = new DeleteUserFromGroupModel();
    deleteUserFromGroupModel.setMessageType(MessageType.DELETER_USER_FROM_GROUP);

    when(JPAService.getInstance().toggleAdminRights(anyString(),anyString())).thenReturn(true);
    boolean flag = toggleModeratorRightsHandler.handleMessage("user", new Gson().toJson(deleteUserFromGroupModel),connection);
    assertFalse(flag);
  }

  @Test
  public void handleMessageExceptionTest() {
    boolean flag = toggleModeratorRightsHandler.handleMessage("user","message",null);
    assertFalse(flag);
  }

  @Test
  public void handleMessageNullUserNameTest() {
    boolean flag = toggleModeratorRightsHandler.handleMessage(null,"message",connection);
    assertFalse(flag);
  }

}
