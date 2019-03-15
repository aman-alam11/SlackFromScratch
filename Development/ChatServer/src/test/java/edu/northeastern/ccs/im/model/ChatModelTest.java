package edu.northeastern.ccs.im.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ChatModelTest {
//  public ChatModel(String from, String to, String conversation, Date time, boolean isDelivered) {

  @Test
  public void test() {
    ChatModel chatModel = new ChatModel();
    assertTrue(chatModel != null);
  }

  @Test
  public void test2() {
    Date date = new Date();
    ChatModel chatModel = new ChatModel("FROM", "TO", "CONVO", date, true);
    assertEquals("CONVO", chatModel.getConversation());
    assertEquals("FROM", chatModel.getSender());
    assertEquals("TO", chatModel.getReciever());
    assertEquals(date, chatModel.getTimeStamp());
    Assert.assertTrue(chatModel.isDelivered());
  }


}
