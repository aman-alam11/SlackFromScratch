package edu.northeastern.ccs.im.model;

import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
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
    UserChat userChat = new UserChat();
    userChat.setDelivered(true);
    userChat.setToUserName("TO");
    userChat.setFromUserName("FROM");
    userChat.setMsg("MESSAGE");
    userChat.setTimestamp(new Date());

    assertEquals("TO", userChat.getToUserName());
    assertEquals("FROM", userChat.getFromUserName());
    assertEquals("MESSAGE", userChat.getMsg());
    assertEquals(new Date(), userChat.getTimestamp());
    assertTrue(userChat.getDelivered());

    userChat.setDelivered(false);
    assertFalse(userChat.getDelivered());
  }

}
