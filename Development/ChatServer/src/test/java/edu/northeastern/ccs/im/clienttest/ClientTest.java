package edu.northeastern.ccs.im.clienttest;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.client.CommandLineMain;
import edu.northeastern.ccs.im.client.IMConnection;
import edu.northeastern.ccs.im.server.Prattle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class ClientTest {

  private IMConnection mImConnection;
  private static final int PORT_NUM = 9000;
  private static final String LOCAL_HOST = "localhost";
  private static final String DUMMY_USER_NAME = "johndoe";

  // Logger for class
  private static final Logger IM_CONNECTION_TEST_LOGGER = Logger
          .getLogger(ClientTest.class.getSimpleName());

  @Test
  public void testConnectionActiveBranchFailed() {
    Prattle.stopServer();

    // Initialize All Tests params
    mImConnection = new IMConnection(LOCAL_HOST, PORT_NUM, DUMMY_USER_NAME);

    OutputStream outputStream = new ByteArrayOutputStream();
    System.setErr(new PrintStream(outputStream));

    try {
      Method loginPrivateMethod = IMConnection.class.getDeclaredMethod("login");
      loginPrivateMethod.setAccessible(true);
      loginPrivateMethod.invoke(mImConnection, null);
      assertTrue(outputStream.toString().contains("If the settings look correct and your machine " +
              "is connected to the Internet, report this error to Dr. Jump"));
      assertTrue(outputStream.toString().contains("ERROR:  Could not make a connection to: "
              + LOCAL_HOST + " at port " + PORT_NUM));
    } catch (Exception e) {
      IM_CONNECTION_TEST_LOGGER.info(e.getMessage());
    }
    System.setErr(System.err);
  }


  @Test
  public void runCommandLine() {
    new Thread(() -> {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      System.setOut(new PrintStream(outputStream));
      CommandLineMain.main(null);
      assertEquals("", outputStream.toString());
    }).start();


  }


}


