package edu.northeastern.ccs.im.server;

import static org.junit.Assert.assertNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import edu.northeastern.ccs.im.ChatLogger;

public class ChatLoggerTest {

	@Test
	public void testInstantiateChat() {
		Constructor<ChatLogger> privateConstructor = null;
		try {
			ChatLogger.error("Error Message");
			privateConstructor = ChatLogger.class.getDeclaredConstructor();
			privateConstructor.setAccessible(true);
			ChatLogger chatLogger = privateConstructor.newInstance();

			Field field = chatLogger.getClass().getDeclaredField("HandlerType");
			field.setAccessible(true);

		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException
				| NoSuchFieldException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
	public void tes() {
		ChatLogger.setMode(ChatLogger.HandlerType.CONSOLE);
		ChatLogger.setMode(ChatLogger.HandlerType.FILE);

		Constructor<ChatLogger> privateConstructor = null;
		try {
			ChatLogger.error("Error Message");
			privateConstructor = ChatLogger.class.getDeclaredConstructor();
			privateConstructor.setAccessible(true);
			privateConstructor.newInstance();

		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException
				| InvocationTargetException e) {
			assertNull(e.getMessage());
		}
	}

	@Test
    public void testLoggerWrite(){
        ChatLogger.warning("message");
    }

	@Test
    public void testLoggerInfo(){
        ChatLogger.info("message");
    }

}
