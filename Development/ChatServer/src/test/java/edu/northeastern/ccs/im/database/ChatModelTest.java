package edu.northeastern.ccs.im.database;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChatModelTest {
  private static final String HELLO = "hello";

  private Chat chat;

  private User user;

  @Before
  public void init() {
    chat = new Chat();
    user = new User();
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
    //chat.isGrpMsg(true);
   // assertTrue(chat.getGrpMsg());
    chat.setIsDelivered(true);
    assertTrue(chat.getIsDelivered());
    chat.setMsg(HELLO);
    assertEquals(HELLO,chat.getMsg());
//    chat.setGroupId(null);
//    assertEquals(1,chat.getGroupId());
  }

  @Test
  public void userTest() {
    user.setId(2);
    assertEquals(2,user.getId());
    user.setEmail(HELLO);
    assertEquals(HELLO,user.getEmail());
    user.setEmail(null);
    assertEquals("",user.getEmail());
    user.setPassword("hel");
    assertEquals("hel",user.getPassword());
  }

}
