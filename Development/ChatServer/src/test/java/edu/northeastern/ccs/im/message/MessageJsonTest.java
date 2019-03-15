package edu.northeastern.ccs.im.message;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class MessageJsonTest {

  @Test
  public void testCreateUserTest() {
    CreateUserMessage createUserMessage = new CreateUserMessage();
    MessageConstants messageConstants = new MessageConstants();

    MessageJson messageJson = new MessageJson(MessageType.USER_CHAT);
    messageJson.setMessageType(MessageType.HELLO);
    messageJson.setFromUser("atti");
    assertEquals("atti",messageJson.getFromUser());
    messageJson.setSendToUser("hello");
    assertEquals("hello",messageJson.getSendToUser());
    messageJson.setCreationTime(new Date());
    assertEquals(Date.class, messageJson.getCreationTime().getClass());
    messageJson.setMessage("hello");
    assertEquals("hello",messageJson.getMessage());

  }
}
