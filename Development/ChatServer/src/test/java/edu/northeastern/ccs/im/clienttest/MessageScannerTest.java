package edu.northeastern.ccs.im.clienttest;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Queue;

import edu.northeastern.ccs.im.client.Message;
import edu.northeastern.ccs.im.client.MessageScanner;

import static org.junit.Assert.assertFalse;

public class MessageScannerTest {

  @Before
  public void init() throws NoSuchFieldException, IllegalAccessException {
    Field messageQueue = MessageScanner.getInstance().getClass().getDeclaredField("messages");
    messageQueue.setAccessible(true);
    Queue<Message> messages = (Queue<Message>) messageQueue.get(MessageScanner.getInstance());
    messages.clear();

  }

  @Test(expected = NoSuchElementException.class)
  public void testNextDirectly() {
    MessageScanner.getInstance().next();
  }

  @Test
  public void testNext() {
    assertFalse(MessageScanner.getInstance().hasNext());
  }


  @Test(expected = UnsupportedOperationException.class)
  public void testRemove() {
    MessageScanner.getInstance().remove();
  }


}
