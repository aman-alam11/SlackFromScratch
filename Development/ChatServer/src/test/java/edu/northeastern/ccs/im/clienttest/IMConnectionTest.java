package edu.northeastern.ccs.im.clienttest;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.client.IMConnection;
import edu.northeastern.ccs.im.client.IllegalNameException;
import edu.northeastern.ccs.im.client.IllegalOperationException;
import edu.northeastern.ccs.im.client.InvalidListenerException;
import edu.northeastern.ccs.im.client.Message;
import edu.northeastern.ccs.im.client.MessageListener;
import edu.northeastern.ccs.im.client.SocketNB;
import edu.northeastern.ccs.im.server.Prattle;

import static edu.northeastern.ccs.im.client.Message.makeNoAcknowledgeMessage;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IMConnectionTest {

  private IMConnection mImConnection;
  private static final int PORT_NUM = 4545;
  private static final String LOCAL_HOST = "localhost";
  private static final String DUMMY_USER_NAME = "johndoe";
  private static final String DEFAULT_USER_NAME = "TooDumbToEnterRealUsername";

  // Logger for class
  private static final Logger IM_CONNECTION_TEST_LOGGER = Logger
          .getLogger(IMConnectionTest.class.getSimpleName());

  static {
    new Thread(() -> {
      try {
        Prattle.main(null);
      } catch (Exception e) {
        IM_CONNECTION_TEST_LOGGER.info(e.getMessage());
      }
    }).start();
  }

  @Before
  public void init() {
    // Initialize All Tests params
    mImConnection = new IMConnection(LOCAL_HOST, PORT_NUM, DUMMY_USER_NAME);
  }


  @Test
  public void testUserName() {
    assertEquals(DUMMY_USER_NAME, mImConnection.getUserName());
  }


  @Test
  public void testEmptyUserName() {
    mImConnection = new IMConnection(LOCAL_HOST, PORT_NUM, "");
    assertEquals(DEFAULT_USER_NAME, mImConnection.getUserName());
  }

  @Test
  public void testNullUserName() {
    mImConnection = new IMConnection(LOCAL_HOST, PORT_NUM, null);
    assertEquals(DEFAULT_USER_NAME, mImConnection.getUserName());
  }

  @Test
  public void testConnectionActive() {
    assertFalse(mImConnection.connectionActive());
  }


  @Test(expected = IllegalOperationException.class)
  public void testSendMessage() {
    mImConnection.sendMessage("Connection is not open");
  }


  @Test(expected = NullPointerException.class)
  public void testDisconnectWithoutConnect() {
    mImConnection = new IMConnection(LOCAL_HOST, PORT_NUM, "%");
    mImConnection.disconnect();
  }

  @Test(expected = InvalidListenerException.class)
  public void testAddMessageListenerNull() {
    mImConnection.addMessageListener(null);
  }

  @Test
  @SuppressWarnings({"squid:S1149", "unchecked"})
  public void testAddMessageListener() {
    try {
      Field messageListenerField = IMConnection.class.getDeclaredField("messageListeners");
      messageListenerField.setAccessible(true);
      Vector<MessageListener> messageListeners =
              (Vector<MessageListener>) messageListenerField.get(mImConnection);
      // Add a dummy Message Listener
      mImConnection.addMessageListener(it -> {
      });
      assertEquals(1, messageListeners.size());
    } catch (NoSuchFieldException | IllegalAccessException e) {
      IM_CONNECTION_TEST_LOGGER.info(e.getMessage());
    }
  }

  @Test(expected = NoSuchElementException.class)
  public void getKeyboardScanner() {
    assertFalse(mImConnection.getKeyboardScanner().hasNext());
    mImConnection.getKeyboardScanner().next();
  }

  @Test(expected = NoSuchElementException.class)
  public void getKeyboardScanner2() {
    mImConnection.getKeyboardScanner().nextLine();
  }


  @Test(expected = IllegalNameException.class)
  public void testIllegalName() {
    mImConnection = new IMConnection(LOCAL_HOST, PORT_NUM, "%");
    mImConnection.connect();
  }

  @Test
  public void testConnectionActiveBranch() {
    assertTrue(mImConnection.connect());
  }

  @Test(expected = IllegalStateException.class)
  public void testReInitPrattle() {
    Prattle.main(null);
  }

  @Test
  public void testConnectionActiveAlternateBranch() {
    mImConnection.connect();
    assertTrue(mImConnection.connectionActive());
    mImConnection.disconnect();
  }

  @Test(expected = IllegalOperationException.class)
  public void testGetMessageScanner() {
    mImConnection.getMessageScanner();
  }

  @Test
  public void sendMessageAlternateBranch() {
    mImConnection.connect();
    mImConnection.sendMessage("It is connected now");
  }

  @Test
  public void fireSendMessagesTest() {
    List<Message> messageList = new ArrayList<>();
    mImConnection.connect();

    try {
      Field messageListenerField = IMConnection.class.getDeclaredField("messageListeners");
      messageListenerField.setAccessible(true);
      Vector<MessageListener> messageListeners =
              (Vector<MessageListener>) messageListenerField.get(mImConnection);

      messageList.add(makeNoAcknowledgeMessage());
      Method sendMessageMethod = IMConnection.class.getDeclaredMethod("fireSendMessages",
              List.class);
      sendMessageMethod.setAccessible(true);
      sendMessageMethod.invoke(mImConnection, messageList);
      assertEquals(1, messageListeners.size());
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
            | NoSuchFieldException e) {
      IM_CONNECTION_TEST_LOGGER.info(e.getMessage());
    }
  }

  @Test
  public void fireStatusChangeTest() {
    mImConnection.connect();

    try {
      Method fireStatusChange = IMConnection.class.getDeclaredMethod("fireStatusChange",
              String.class);
      fireStatusChange.setAccessible(true);
      fireStatusChange.invoke(mImConnection, DUMMY_USER_NAME);

    } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
      IM_CONNECTION_TEST_LOGGER.info(e.getMessage());
    }
  }

  @Test
  public void testLogOut() {
    try {
      Method logoutMethod = IMConnection.class.getDeclaredMethod("loggedOut");
      logoutMethod.setAccessible(true);

      Field field = IMConnection.class.getDeclaredField("socketConnection");
      field.setAccessible(true);
      SocketNB socketConnection = (SocketNB) field.get(mImConnection);

      mImConnection.connect();
      logoutMethod.invoke(mImConnection, null);
      assertNull(socketConnection);

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
            | NoSuchFieldException e) {
      IM_CONNECTION_TEST_LOGGER.info(e.getMessage());
    }
  }

}


