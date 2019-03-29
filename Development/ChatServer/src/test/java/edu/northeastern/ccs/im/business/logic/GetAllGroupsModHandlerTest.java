package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.GetAllGroupsModHandler;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class GetAllGroupsModHandlerTest {

  private GetAllGroupsModHandler getAllGroupsModHandler;

  @Mock
  private Connection connection;

  @Mock
  private JPAService jpaService;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    JPAService.setJPAService(jpaService);
    getAllGroupsModHandler = new GetAllGroupsModHandler();
  }

  @Test
  public void handleMessageTrueTest() {

    MessageJson messageJson = new MessageJson("str", MessageType.GET_ALL_GROUPS_MOD,"");

    Map<String, Boolean> map = new HashMap<>();
    map.put("str", true);
    when(JPAService.getInstance().getAllGroupsForUser(any())).thenReturn(map);
    boolean  b = getAllGroupsModHandler.handleMessage("user", new Gson().toJson(messageJson),connection);
    assertTrue(b);

  }

  @Test
  public void handleMessageUserNullTest() {

    MessageJson messageJson = new MessageJson("str", MessageType.GET_ALL_GROUPS_MOD,"");

    Map<String, Boolean> map = new HashMap<>();
    map.put("str", true);
    when(JPAService.getInstance().getAllGroupsForUser(any())).thenReturn(map);
    boolean  b = getAllGroupsModHandler.handleMessage(null, new Gson().toJson(messageJson),connection);
    assertFalse(b);
  }

  @Test
  public void handleMessageNotAllGroupsTest() {

    MessageJson messageJson = new MessageJson("str", MessageType.USER_CHAT,"");

    Map<String, Boolean> map = new HashMap<>();
    map.put("str", true);
    when(JPAService.getInstance().getAllGroupsForUser(any())).thenReturn(map);
    boolean  b = getAllGroupsModHandler.handleMessage("usr", new Gson().toJson(messageJson),connection);
    assertFalse(b);
  }

  @Test
  public void handleMessageExceptionTest() {

    MessageJson messageJson = new MessageJson("str", MessageType.GET_ALL_GROUPS_MOD,"");

    Map<String, Boolean> map = new HashMap<>();
    map.put("str", true);
    when(JPAService.getInstance().getAllGroupsForUser(any())).thenReturn(map);
    boolean  b = getAllGroupsModHandler.handleMessage("usr", new Gson().toJson(messageJson),null);
    assertFalse(b);
  }

}
