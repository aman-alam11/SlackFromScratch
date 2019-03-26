package edu.northeastern.ccs.im.business.logic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.business.logic.ChatHandler;
import edu.northeastern.ccs.im.server.business.logic.JsonMessageHandlerFactory;
import edu.northeastern.ccs.im.server.business.logic.LoginHandler;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;
import edu.northeastern.ccs.im.server.business.logic.UserCreationHandler;
import edu.northeastern.ccs.im.server.business.logic.UserSearchHandler;

public class JsonMessageHandlerFactoryTest {

  private JsonMessageHandlerFactory jsonMessageHandlerFactory;



  @Before
  public void init() {
    jsonMessageHandlerFactory = new JsonMessageHandlerFactory();
  }

  @Test
  public void handleMessageTest() {
    MessageHandler messageHandlerLogin = jsonMessageHandlerFactory.getMessageHandler(MessageType.LOGIN);
    assertEquals(LoginHandler.class, messageHandlerLogin.getClass());

    MessageHandler messageHandlerUserCreation = jsonMessageHandlerFactory.getMessageHandler(MessageType.CREATE_USER);
    assertEquals(UserCreationHandler.class, messageHandlerUserCreation.getClass());

    MessageHandler messageHandlerChat = jsonMessageHandlerFactory.getMessageHandler(MessageType.USER_CHAT);
    assertEquals(ChatHandler.class, messageHandlerChat.getClass());

    MessageHandler messageHandlerSearch = jsonMessageHandlerFactory.getMessageHandler(MessageType.USER_SEARCH);
    assertEquals(UserSearchHandler.class, messageHandlerSearch.getClass());


    MessageHandler messageHandlerDefault = jsonMessageHandlerFactory.getMessageHandler(MessageType.HELLO);
    assertEquals(LoginHandler.class, messageHandlerDefault.getClass());


  }
}
