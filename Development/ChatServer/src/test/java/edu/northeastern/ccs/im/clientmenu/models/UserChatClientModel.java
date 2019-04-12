package edu.northeastern.ccs.im.clientmenu.models;

import org.junit.Before;
import org.junit.Test;

import edu.northeastern.ccs.im.model.ChatModel;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserChatClientModel {


  private ChatModel userChat;
  private static final String MESSAGE = "message";

  @Before
  public void init() {
     userChat = new ChatModel();
  }

  @Test
  public void testNull() {

    assertNull(userChat.getErrorCode());
    assertNull(userChat.getDelivered());
    assertNull(userChat.getExpiry());
    assertNull(userChat.getTimestamp());
    assertNull(userChat.getMsg());
    assertNull(userChat.getToUserName());
    assertNull(userChat.getToUserName());

  }

  @Test
  public void testGetters() {

    userChat.setFromUserName("from");
    assertEquals("from",userChat.getFromUserName());

    userChat.setMsg(MESSAGE);
    assertEquals(MESSAGE,userChat.getMsg());

    userChat.setToUserName("to");
    assertEquals("to",userChat.getToUserName());

    userChat.setDelivered(true);
    assertTrue(true);

    userChat.setTimestamp(new Date());
    assertEquals(Date.class,userChat.getTimestamp().getClass());
  }


}
