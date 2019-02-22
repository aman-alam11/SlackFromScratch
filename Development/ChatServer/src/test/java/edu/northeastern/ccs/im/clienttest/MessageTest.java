package edu.northeastern.ccs.im.clienttest;


import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.MessageType;
import edu.northeastern.ccs.im.client.Message;

import static edu.northeastern.ccs.im.client.Message.makeAcknowledgeMessage;
import static edu.northeastern.ccs.im.client.Message.makeBroadcastMessage;
import static edu.northeastern.ccs.im.client.Message.makeQuitMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class MessageTest {

  private static final String DUMMY_SENDER_NAME = "JOHN DOE";
  private static final String DUMMY_BROADCAST_MESSAGE = "This is a broadcast message";
  private static final Logger MESSAGE_TEST_LOGGER = Logger
          .getLogger(MessageTest.class.getSimpleName());

  @Test
  public void testMakeMessageQuit() {
    assertEquals(DUMMY_SENDER_NAME, makeQuitMessage(DUMMY_SENDER_NAME).getSender());
    assertNull(makeQuitMessage(DUMMY_SENDER_NAME).getText());
    assertEquals("BYE 8 JOHN DOE 2 --", makeQuitMessage(DUMMY_SENDER_NAME).toString());

    assertFalse(makeQuitMessage(DUMMY_SENDER_NAME).isAcknowledge());
    assertFalse(makeQuitMessage(DUMMY_SENDER_NAME).isBroadcastMessage());
    assertFalse(makeQuitMessage(DUMMY_SENDER_NAME).isDisplayMessage());
    assertFalse(makeQuitMessage(DUMMY_SENDER_NAME).isInitialization());
    assertTrue(makeQuitMessage(DUMMY_SENDER_NAME).terminate());
  }

  @Test
  public void testMakeMessageBroadcast() {
    assertEquals(DUMMY_SENDER_NAME, makeBroadcastMessage(DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE)
            .getSender());
    assertEquals(DUMMY_BROADCAST_MESSAGE, makeBroadcastMessage(DUMMY_SENDER_NAME,
            DUMMY_BROADCAST_MESSAGE)
            .getText());
    assertEquals("BCT 8 JOHN DOE 27 This is a broadcast message",
            makeBroadcastMessage(DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE).toString());

    assertFalse(makeBroadcastMessage(DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE).isAcknowledge());
    assertTrue(makeBroadcastMessage(DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE).isBroadcastMessage());
    assertTrue(makeBroadcastMessage(DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE).isDisplayMessage());
    assertFalse(makeBroadcastMessage(DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE).isInitialization());
    assertFalse(makeBroadcastMessage(DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE).terminate());
  }


  @Test
  public void testMakeAckMessage() {
    assertTrue(makeAcknowledgeMessage(DUMMY_SENDER_NAME).isAcknowledge());
    assertFalse(makeAcknowledgeMessage(DUMMY_SENDER_NAME).isBroadcastMessage());
    assertFalse(makeAcknowledgeMessage(DUMMY_SENDER_NAME).isDisplayMessage());
    assertFalse(makeAcknowledgeMessage(DUMMY_SENDER_NAME).isInitialization());
    assertFalse(makeAcknowledgeMessage(DUMMY_SENDER_NAME).terminate());

    assertEquals("ACK",
            makeAcknowledgeMessage(DUMMY_SENDER_NAME).getType() + "");
  }

  @Test
  public void testMakeHello() {
    try {
      Method makeHelloMethod = Message.class.getDeclaredMethod("makeHelloMessage", String.class);
      makeHelloMethod.setAccessible(true);
      Message helloMessage = (Message) makeHelloMethod.invoke(null, DUMMY_SENDER_NAME);
      assertEquals("HLO 2 -- 8 JOHN DOE", helloMessage.toString());

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      MESSAGE_TEST_LOGGER.info(e.getMessage());
    }
  }


  @Test
  public void makeMessageTest() {
    try {
      Method makeMessageMethod = Message.class.getDeclaredMethod("makeMessage", String.class,
              String.class, String.class);
      makeMessageMethod.setAccessible(true);
      Message quitMessage = (Message) makeMessageMethod.invoke(null,
              MessageType.QUIT.toString(), DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE);
      assertEquals("BYE 8 JOHN DOE 2 --", quitMessage.toString());

      Message broadcastMessage = (Message) makeMessageMethod.invoke(null,
              MessageType.BROADCAST.toString(), DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE);
      assertEquals("BCT 8 JOHN DOE 27 This is a broadcast message",
              broadcastMessage.toString());

      Message helloMessage = (Message) makeMessageMethod.invoke(null,
              MessageType.HELLO.toString(), DUMMY_SENDER_NAME, DUMMY_BROADCAST_MESSAGE);
      assertEquals("HLO 8 JOHN DOE 2 --", helloMessage.toString());
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      MESSAGE_TEST_LOGGER.info(e.getMessage());
    }
  }

}
