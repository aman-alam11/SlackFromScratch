package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.ToggleModeratorRightsHandler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ToggleModeratorRightsHandlerTest {

  @Mock
  private Connection connection;
  private ToggleModeratorRightsHandler toggleModeratorRightsHandler;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    toggleModeratorRightsHandler = new ToggleModeratorRightsHandler();
  }

  @Test
  public void handleMessageTest() {
    List<String> listKeys = new ArrayList<>();
    listKeys.add("user");
    listKeys.add("uaer1");
    boolean flag = toggleModeratorRightsHandler.handleMessage("user", new Gson().toJson(listKeys),connection);
    assertTrue(flag);
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
