package edu.northeastern.ccs.im.clienttest;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.client.SocketNB;
import edu.northeastern.ccs.im.server.Prattle;

import static org.junit.Assert.assertTrue;

public class SocketNBTest {

  private static final String HOST_NAME = "localhost";
  private static final int PORT_NUM = 4545;
  private SocketNB mSocketNB;
  private static final Logger SOCKET_TEST_LOGGER = Logger.getLogger(SocketNBTest.class.getSimpleName());

  static {
    new Thread(() -> Prattle.main(null)).start();
  }

  @Before
  public void init() throws NoSuchMethodException {
    mSocketNB = new SocketNB(HOST_NAME, PORT_NUM);

    Method startConnection = SocketNB.class.getDeclaredMethod("startIMConnection");
    startConnection.setAccessible(true);
    try {
      startConnection.invoke(mSocketNB);
    } catch (IllegalAccessException | InvocationTargetException e) {
      SOCKET_TEST_LOGGER.info(e.getMessage());
    }
  }

  @Test
  public void testConnection() throws
          NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    Method methodCheckConn = SocketNB.class.getDeclaredMethod("isConnected");
    methodCheckConn.setAccessible(true);
    boolean isConnected = (Boolean) methodCheckConn.invoke(mSocketNB);
    assertTrue(isConnected);

    Method closeMethod = SocketNB.class.getDeclaredMethod("close");
    closeMethod.setAccessible(true);
    closeMethod.invoke(mSocketNB);
  }


  @Test
  public void testReadArgs() {
    try {
      Method readArgsMethod = SocketNB.class.getDeclaredMethod("readArgument", CharBuffer.class);
      readArgsMethod.setAccessible(true);
      char[] randomChar = new char[10];
      randomChar[0] = 'M';
      randomChar[1] = 'S';
      randomChar[2] = 'D';

      CharBuffer charBuffer = CharBuffer.wrap(randomChar);
      readArgsMethod.invoke(mSocketNB, charBuffer);
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      SOCKET_TEST_LOGGER.info(e.getMessage());
    }
  }
}
