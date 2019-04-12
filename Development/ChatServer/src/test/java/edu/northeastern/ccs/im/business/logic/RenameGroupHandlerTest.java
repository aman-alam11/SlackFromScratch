package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.RenameGroupHandler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RenameGroupHandlerTest {
  @Mock
  private Connection connection;
  private RenameGroupHandler renameGroupHandler;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    renameGroupHandler = new RenameGroupHandler();
  }

  @Test
  public void handleMessageTest() {
    List<String> listKeys = new ArrayList<>();
    listKeys.add("user");
    listKeys.add("uaer1");
    boolean flag = renameGroupHandler.handleMessage("user", new Gson().toJson(listKeys),connection);
    assertTrue(flag);
  }

  @Test
  public void handleMessageExceptionTest() {
    boolean flag = renameGroupHandler.handleMessage("user","message",null);
    assertFalse(flag);
  }
}
