package edu.northeastern.ccs.im.business.logic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.UnreadMessageHandler;

public class UnreadMessageHandlerTest {

  private UnreadMessageHandler unreadMessageHandler;

  @Mock
  private Connection connection;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    unreadMessageHandler = new UnreadMessageHandler();
  }

  //@Test
  public void handleMessageTest() {
    unreadMessageHandler.handleMessage("user","message",connection);
  }

  @Test
  public void handleMessageExceptionTest() {
    boolean flag = unreadMessageHandler.handleMessage("user","message",null);
    assertFalse(flag);
  }

  @Test
  public void handleMessageNullUserNameTest() {
    boolean flag = unreadMessageHandler.handleMessage(null,"message",connection);
    assertFalse(flag);
  }
}
