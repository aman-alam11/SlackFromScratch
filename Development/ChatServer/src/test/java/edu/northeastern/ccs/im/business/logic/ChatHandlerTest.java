package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


import edu.northeastern.ccs.im.clientmenu.models.UserChat;
import edu.northeastern.ccs.im.server.Connection;


public class ChatHandlerTest {

  private ChatHandler chatHandler;
  private Gson mGson;

  @Mock
  private Connection connection;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    chatHandler = new ChatHandler();
    mGson = new Gson();
  }

  @Test
  public void handleMessageTest() {
    UserChat userChat = new UserChat();
    userChat.setFromUserName("atti");
    userChat.setMsg("hello");
    userChat.setToUserName("rsa");
    String message = mGson.toJson(userChat);

    boolean b = chatHandler.handleMessage("user",message,connection);
    assertFalse(b);
  }
}
