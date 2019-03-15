package edu.northeastern.ccs.im.database;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatModelTest {

  private Chat chat;

  @Before
  public void init() {
    chat = new Chat();
  }

  @Test
  public void testId() {
    chat.setId(1);
    assertEquals(1 ,chat.getId());
    chat.setFromId(new User());
    assertEquals(User.class, chat.getFromId().getClass());
    chat.setToId(new User());
    assertEquals(User.class, chat.getToId().getClass());
    chat.setCreated(new Date());
    assertEquals(Date.class, chat.getCreated().getClass());
    chat.setExpiry(new Date());
    assertEquals(Date.class, chat.getExpiry().getClass());
    chat.setGrpMsg(true);
    assertTrue(chat.getGrpMsg());
    chat.setIsDelivered(true);
    assertTrue(chat.getIsDelivered());
    chat.setMsg("hello");
    assertEquals("hello",chat.getMsg());
    chat.setReplyTo(1);
    assertEquals(1,chat.getReplyTo());

  }

}
