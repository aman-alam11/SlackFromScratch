package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.ChatHandler;


public class ChatHandlerTest {

  private ChatHandler chatHandler;
  private Gson mGson;

  @Mock
  private Connection connection;

  @Mock
  private JPAService jpaService;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    JPAService.setJPAService(jpaService);
    chatHandler = new ChatHandler();
    mGson = new Gson();
  }

  @Test
  public void handleMessageTrueTest() {
    ChatModel userChat = new ChatModel();
    userChat.setFromUserName("atti");
    userChat.setMsg("hello");
    userChat.setToUserName("rsa");
    String message = mGson.toJson(userChat);

    long num  = 1;
    when(JPAService.getInstance().createChatMessage(any())).thenReturn(num);

    boolean b = chatHandler.handleMessage("user",message,connection);
    assertTrue(b);


  }

  @Test
  public void handleMessageFalseTest() {
  	ChatModel userChat = new ChatModel();
    userChat.setFromUserName("atti");
    userChat.setMsg("hello");
    userChat.setToUserName("rsa");
    String message = mGson.toJson(userChat);

    long num  = 0;
    when(JPAService.getInstance().createChatMessage(any())).thenReturn(num);

    boolean b = chatHandler.handleMessage("user",message,connection);
    assertFalse(b);
  }
}
