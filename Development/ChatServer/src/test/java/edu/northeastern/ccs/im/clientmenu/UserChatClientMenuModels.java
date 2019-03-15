package edu.northeastern.ccs.im.clientmenu;

import org.junit.Test;

import java.util.Date;

import edu.northeastern.ccs.im.clientmenu.models.UserChat;

import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class UserChatClientMenuModels {

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
  public void testGetSet() {
    Date date = new Date();
    UserChat userChat = new UserChat();
    userChat.setDelivered(true);
    userChat.setToUserName("TO");
    userChat.setFromUserName("FROM");
    userChat.setMsg("MESSAGE");
    userChat.setTimestamp(date);
    userChat.setExpiry(date);
    userChat.setErrorCode("404");

    assertEquals("TO", userChat.getToUserName());
    assertEquals("FROM", userChat.getFromUserName());
    assertEquals("MESSAGE", userChat.getMsg());
    assertEquals(date, userChat.getTimestamp());
    assertTrue(userChat.getDelivered());
    assertEquals(date, userChat.getExpiry());
    assertEquals("404", userChat.getErrorCode());

    userChat.setDelivered(false);
    assertFalse(userChat.getDelivered());

    assertEquals("FROM: MESSAGE", userChat.toString());
  }

}
