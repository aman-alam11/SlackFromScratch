package edu.northeastern.ccs.im.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserChatTest {

  @Test
  public void testNulls() {
    UserChat userChat = new UserChat();
    assertNull(userChat.getToUserName());
    assertNull(userChat.getFromUserName());
    assertNull(userChat.getExpiry());
    assertNull(userChat.getMsg());
    assertNull(userChat.getDelivered());
    assertNull(userChat.getErrorCode());
    assertNull(userChat.getTimestamp());
  }


  @Test
  public void testGetSet(){
    Date date = new Date();
    UserChat userChat = new UserChat();
    userChat.setDelivered(true);
    userChat.setToUserName("TO");
    userChat.setFromUserName("FROM");
    userChat.setMsg("MESSAGE");
    userChat.setTimestamp(date);

    assertEquals("TO", userChat.getToUserName());
    assertEquals("FROM", userChat.getFromUserName());
    assertEquals("MESSAGE", userChat.getMsg());
    assertEquals(date, userChat.getTimestamp());
    assertTrue(userChat.getDelivered());

    userChat.setDelivered(false);
    assertFalse(userChat.getDelivered());
  }

}
