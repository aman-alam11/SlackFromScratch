package edu.northeastern.ccs.im.servertest;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.server.ServerConstants;

import static org.junit.Assert.assertEquals;


public class ServerConstantsTest {

  private static final int PORT = 4545;
  private static final int DELAY_IN_MS = 50;
  private static final int THREAD_POOL_SIZE = 20;
  private static final int CLIENT_CHECK_DELAY = 200;
  private static final String SERVER_NAME = "Prattle";
  private static final String BOUNCER_ID = "Bouncer";


  @Test
  public void testConstants() {
    try {
      Field portField = ServerConstants.class.getDeclaredField("PORT");
      portField.setAccessible(true);
      int portNum = (int) portField.get(ServerConstants.class);
      assertEquals(PORT, portNum);

      Field delayInMsField = ServerConstants.class.getDeclaredField("DELAY_IN_MS");
      delayInMsField.setAccessible(true);
      int delayNum = (int) delayInMsField.get(ServerConstants.class);
      assertEquals(DELAY_IN_MS, delayNum);

      Field threadPoolSizeField = ServerConstants.class.getDeclaredField("THREAD_POOL_SIZE");
      threadPoolSizeField.setAccessible(true);
      int threadPoolSizeNum = (int) threadPoolSizeField.get(ServerConstants.class);
      assertEquals(THREAD_POOL_SIZE, threadPoolSizeNum);

      Field clientCheckDelayField = ServerConstants.class.
              getDeclaredField("CLIENT_CHECK_DELAY");
      clientCheckDelayField.setAccessible(true);
      int clientCheckDelayFieldNum = (int) clientCheckDelayField.get(ServerConstants.class);
      assertEquals(CLIENT_CHECK_DELAY, clientCheckDelayFieldNum);

      Field serverNameField = ServerConstants.class.getDeclaredField("SERVER_NAME");
      serverNameField.setAccessible(true);
      String serverName = (String) serverNameField.get(ServerConstants.class);
      assertEquals(SERVER_NAME, serverName);

      Field bouncerIdField = ServerConstants.class.getDeclaredField("BOUNCER_ID");
      bouncerIdField.setAccessible(true);
      String bouncerIdString = (String) bouncerIdField.get(ServerConstants.class);
      assertEquals(BOUNCER_ID, bouncerIdString);

      Constructor<ServerConstants> privateConstructor =
              ServerConstants.class.getDeclaredConstructor();
      privateConstructor.setAccessible(true);
      privateConstructor.newInstance();
      assertEquals("edu.northeastern.ccs.im.server.ServerConstants",
              privateConstructor.getName());

    } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException
            | InstantiationException | InvocationTargetException e) {
      Logger.getLogger(ServerConstantsTest.class.getSimpleName()).info(e.getMessage());
    }
  }

}
