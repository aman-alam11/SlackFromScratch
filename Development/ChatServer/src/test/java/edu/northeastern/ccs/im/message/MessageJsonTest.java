package edu.northeastern.ccs.im.message;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("all")
public class MessageJsonTest {

  private static final String HELLO = "hello";


  @Test
  public void testCreateUserTest() {
    CreateUserMessage createUserMessage = new CreateUserMessage();
    MessageConstants messageConstants = new MessageConstants();

    MessageJson messageJson = new MessageJson(MessageType.USER_CHAT);
    messageJson.setMessageType(MessageType.HELLO);
    messageJson.setFromUser("atti");
    assertEquals("atti",messageJson.getFromUser());
    messageJson.setSendToUser(HELLO);
    assertEquals(HELLO,messageJson.getSendToUser());
    messageJson.setCreationTime(new Date());
    assertEquals(Date.class, messageJson.getCreationTime().getClass());
    messageJson.setMessage(HELLO);
    assertEquals(HELLO,messageJson.getMessage());

  }
}
