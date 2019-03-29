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
import edu.northeastern.ccs.im.server.business.logic.GetAllUsersForGroup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class GetAllUsersForGroupTest {

  private GetAllUsersForGroup getAllUsersForGroup;

  @Mock
  private Connection connection;

  @Mock
  private JPAService jpaService;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    JPAService.setJPAService(jpaService);
    getAllUsersForGroup = new GetAllUsersForGroup();
  }

  @Test
  public void handleMessageTrueTest() {

    MessageJson messageJson = new MessageJson("str", MessageType.GET_ALL_USERS_FOR_GRP,"");

    Map<String, Boolean> map = new HashMap<>();
    map.put("str", true);
    when(JPAService.getInstance().getAllUsersForGroup(any())).thenReturn(map);
    boolean b = getAllUsersForGroup.handleMessage("user", new Gson().toJson(messageJson),connection);
    assertTrue(b);

  }

  @Test
  public void handleMessageUserNullTest() {

    MessageJson messageJson = new MessageJson("str", MessageType.GET_ALL_USERS_FOR_GRP,"");

    Map<String, Boolean> map = new HashMap<>();
    map.put("str", true);
    when(JPAService.getInstance().getAllUsersForGroup(any())).thenReturn(map);
    boolean b = getAllUsersForGroup.handleMessage(null, new Gson().toJson(messageJson),connection);
    assertFalse(b);

  }

  @Test
  public void handleMessageNotAllGroupsTest() {

    MessageJson messageJson = new MessageJson("str", MessageType.USER_CHAT,"");

    Map<String, Boolean> map = new HashMap<>();
    map.put("str", true);
    when(JPAService.getInstance().getAllUsersForGroup(any())).thenReturn(map);
    boolean b = getAllUsersForGroup.handleMessage("usr", new Gson().toJson(messageJson),connection);
    assertFalse(b);
  }

  @Test
  public void handleMessageExceptionTest() {

    MessageJson messageJson = new MessageJson("str", MessageType.GET_ALL_USERS_FOR_GRP,"");

    Map<String, Boolean> map = new HashMap<>();
    map.put("str", true);
    when(JPAService.getInstance().getAllUsersForGroup(any())).thenReturn(map);
    boolean b =getAllUsersForGroup.handleMessage("usr", new Gson().toJson(messageJson),null);
    assertFalse(b);
  }
}
