package edu.northeastern.ccs.im.server;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import edu.northeastern.ccs.im.ChatLogger;

import static org.junit.Assert.assertNull;

public class ChatLoggerTest {

  @Test
  public void testInstantiateChat()  {
    Constructor<ChatLogger> privateConstructor = null;
    try {
      ChatLogger.error("Error Message");
      privateConstructor = ChatLogger.class.getDeclaredConstructor();
      privateConstructor.setAccessible(true);
      ChatLogger chatLogger = privateConstructor.newInstance();

      Field field = chatLogger.getClass().getDeclaredField("HandlerType");
      field.setAccessible(true);

    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException e) {
      assertNull(e.getMessage());
    }


  }

  @Test
  public void tes(){
    ChatLogger.setMode(ChatLogger.HandlerType.CONSOLE);
    ChatLogger.setMode(ChatLogger.HandlerType.FILE);
  }
}
