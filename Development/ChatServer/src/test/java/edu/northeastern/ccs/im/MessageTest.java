package edu.northeastern.ccs.im;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Test;

import edu.northeastern.ccs.im.message.MessageType;

public class MessageTest {

  private static final Logger LOGGER = Logger.getLogger(
          MessageTest.class.getName());

  private Message message = null;
  private static final String CLIENTNAME = "hello";
  private static final String MESSAGE_TEXT = "message";


  @Test
  public void quitMessageClientTextTest(){
    message = Message.makeQuitMessage(CLIENTNAME);
    assertNull(null, message.getText());
  }

  @Test
  public void quitMessageClientNameTest(){
    message = Message.makeQuitMessage(CLIENTNAME);
    assertEquals(CLIENTNAME, message.getName());
  }

  @Test
  public void helloMessageClientTextTest(){
    message = Message.makeHelloMessage(CLIENTNAME);
    assertEquals(CLIENTNAME, message.getText());
  }

  @Test
  public void helloMessageClientNameTest(){
    message = Message.makeHelloMessage(CLIENTNAME);
    assertNull(null, message.getName());
  }

  @Test
  public void broadcastMessageTextTest(){
    String messageText = "world";
    message = Message.makeBroadcastMessage(CLIENTNAME,messageText);
    assertEquals(messageText, message.getText());
  }

  @Test
  public void broadcaseMessageClientNameTest(){
    String messageText = "world";
    message = Message.makeBroadcastMessage(CLIENTNAME, messageText);
    assertEquals(CLIENTNAME, message.getName());
  }

  @Test
  public void broadcastMessageClientTextTest(){
    String messageText = "local";
    message = Message.makeBroadcastMessage(CLIENTNAME, messageText);
    assertEquals(messageText, message.getText());
  }

  @Test
  public void simpleLoginMessageNameTest(){
    message = Message.makeSimpleLoginMessage(CLIENTNAME);
    assertEquals(CLIENTNAME, message.getName());
  }

  @Test
  public void simpleLoginMessageClientTextTest(){
    message = Message.makeSimpleLoginMessage(MESSAGE_TEXT);
    assertNull(null, message.getText());
  }

  @Test
  public void isBroadCastMessageTest(){
    message = Message.makeBroadcastMessage(MESSAGE_TEXT,CLIENTNAME);
    assertTrue(message.isBroadcastMessage());
  }

  @Test
  public void isNotBroadCastMessageTest(){
    message = Message.makeSimpleLoginMessage(MESSAGE_TEXT);
    assertFalse(message.isBroadcastMessage());
  }

  @Test
  public void isInitializeMessageTest(){
    message = Message.makeSimpleLoginMessage(MESSAGE_TEXT);
    assertTrue(message.isInitialization());
  }

  @Test
  public void isNotInitializeMessageTest(){
    message = Message.makeBroadcastMessage(MESSAGE_TEXT,CLIENTNAME);
    assertFalse(message.isInitialization());
  }

  @Test
  public void isQuitMessageTest(){
    message = Message.makeQuitMessage(MESSAGE_TEXT);
    assertTrue(message.terminate());
  }

  @Test
  public void isNotQuitMessageTest(){
    message = Message.makeSimpleLoginMessage(MESSAGE_TEXT);
    assertFalse(message.terminate());
  }

  @Test
  public void makeMessageSimpleLoginIsInitializeTest(){
    String type = "HLO";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertTrue(message.isInitialization());
  }

  @Test
  public void makeMessageSimpleLoginIsNotInitializeTest(){
    String type = "BCT";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertFalse(message.isInitialization());
  }

  @Test
  public void makeMessageSimpleLoginNameTest(){
    String type = "HLO";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertEquals(CLIENTNAME, message.getName());
  }

  @Test
  public void makeMessageSimpleLoginTextTest(){
    String type = "HLO";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertNull(null, message.getText());
  }


  @Test
  public void makeMessageQuitIsTerminateTest(){
    String type = "BYE";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertTrue(message.terminate());
  }

  @Test
  public void makeMessageQuitIsNotTerminateTest(){
    String type = "BCT";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertFalse(message.terminate());
  }

  @Test
  public void makeMessageQuitNameTest(){
    String type = "BYE";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertEquals(CLIENTNAME, message.getName());
  }

  @Test
  public void makeMessageQuitTextTest(){
    String type = "BYE";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertNull(null, message.getText());
  }

  @Test
  public void makeMessageBroadCastIsBroadCastTest(){
    String type = "BCT";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertTrue(message.isBroadcastMessage());
  }

  @Test
  public void makeMessageBroadCastIsNotBroadCastTest(){
    String type = "BYE";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertFalse(message.isBroadcastMessage());
  }

  @Test
  public void makeMessageBroadCastNameTest(){
    String type = "BCT";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertEquals(CLIENTNAME, message.getName());
  }

  @Test
  public void makeMessageBroadCastTextTest(){
    String type = "BCT";
    String text = "text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertEquals(text, message.getText());
  }

  @Test
  public void makeMessageNullTest(){
    String type = "ABC";
    String text = "text";
    assertNull(Message.makeMessage(type, CLIENTNAME, text));
  }

  @Test
  public void toStringInitializeTest(){
    String type = "HLO";
    String text = "text";
    String testString = "HLO 5 hello 2 --";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertEquals(testString, message.toString());
  }

  @Test
  public void toStringQuitTest(){
    String type = "BYE";
    String text = "text";
    String testString = "BYE 5 hello 2 --";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertEquals(testString, message.toString());
  }

  @Test
  public void toStringBroadCastTest(){
    String type = "BCT";
    String text = "text";
    String testString = "BCT 5 hello 4 text";
    message = Message.makeMessage(type, CLIENTNAME, text);
    assertEquals(testString, message.toString());
  }

  @Test
  public void toStringNullTest() {
    try {
      Constructor<Message> declaredMethod= Message.class.getDeclaredConstructor(MessageType.class,
              String.class, String.class);
      declaredMethod.setAccessible(true);
      Message cons = declaredMethod.newInstance( MessageType.HELLO, null, "TEXT");
      String testString = "HLO 2 -- 4 TEXT";
      Assert.assertEquals(testString, cons.toString());
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      LOGGER.info(e.toString());
    }
  }

}
