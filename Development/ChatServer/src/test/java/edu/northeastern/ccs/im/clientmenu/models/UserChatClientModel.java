package edu.northeastern.ccs.im.clientmenu.models;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserChatClientModel {


  private UserChat userChat;

  @Before
  public void init() {
     userChat = new UserChat();
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

    userChat.setMsg("message");
    assertEquals("message",userChat.getMsg());

    userChat.setToUserName("to");
    assertEquals("to",userChat.getToUserName());

    userChat.setDelivered(true);
    assertTrue(true);

    userChat.setTimestamp(new Date());
    assertEquals(Date.class,userChat.getTimestamp().getClass());
  }

  @Test
  public void toStringTest() {
    userChat.setFromUserName("from");
    userChat.setMsg("message");
    assertEquals("from: message",userChat.toString());
  }



}
