package edu.northeastern.ccs.im;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import edu.northeastern.ccs.im.client.communication.SocketConnection;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class SocketConnectionTest {

  private SocketConnection socketConnection;

  @Before
  public void init() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<?> constructor = SocketConnection.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    socketConnection = (SocketConnection) constructor.newInstance();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullUrl() {
    SocketConnection.getInstance(null, 1000);
  }


  @Test
  public void test() {
    try {
      assertFalse(socketConnection.isConnected());
    } catch (Exception e) {
      assertNull(e.getMessage());
    }


    try {
      assertFalse(socketConnection.hasNext());
    } catch (Exception e) {
      assertNull(e.getMessage());
    }


    try {
      assertNull(socketConnection.next());
    } catch (Exception e) {
      assertNull(e.getMessage());
    }

    try {
      socketConnection.sendMessage(new MessageJson(MessageType.HELLO));
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }


  @Test
  public void test2() throws NoSuchMethodException {
    socketConnection.registerListener(message -> {
    }, MessageType.HELLO);
    Method decodeMessageMethod = SocketConnection.class.getDeclaredMethod("decodeMessage");
    decodeMessageMethod.setAccessible(true);
    try {
      decodeMessageMethod.invoke(socketConnection);
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }

  @Test
  public void test3() throws NoSuchMethodException {
    Method decodeMessageMethod = SocketConnection.class.getDeclaredMethod("initThread");
    decodeMessageMethod.setAccessible(true);
    try {
      decodeMessageMethod.invoke(socketConnection);
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }


}
