package edu.northeastern.ccs.im.business.logic;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.business.logic.handler.AddGroupUsersHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.ChatHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.DeleteGroupHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.GetAllGroupsModHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.GetAllUsersForGroup;
import edu.northeastern.ccs.im.server.business.logic.handler.GroupChatHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.GroupCreationHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.GroupSearchHandler;
import edu.northeastern.ccs.im.server.business.logic.JsonMessageHandlerFactory;
import edu.northeastern.ccs.im.server.business.logic.handler.LoginHandler;
import edu.northeastern.ccs.im.server.business.logic.MessageHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.RenameGroupHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.ToggleModeratorRightsHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.UnreadMessageHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.UserCreationHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.UserSearchHandler;

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


    MessageHandler messageHandlercreateGroup = jsonMessageHandlerFactory.getMessageHandler(MessageType.CREATE_GROUP);
    assertEquals(GroupCreationHandler.class, messageHandlercreateGroup.getClass());

    MessageHandler messageHandlerGroupChat = jsonMessageHandlerFactory.getMessageHandler(MessageType.GROUP_CHAT);
    assertEquals(GroupChatHandler.class, messageHandlerGroupChat.getClass());


    MessageHandler messageHandlerAddUserInGroup = jsonMessageHandlerFactory.getMessageHandler(MessageType.ADD_USER_IN_GROUP);
    assertEquals(AddGroupUsersHandler.class, messageHandlerAddUserInGroup.getClass());

    MessageHandler messageHandlerUnreadMessages = jsonMessageHandlerFactory.getMessageHandler(MessageType.UNREAD_MSG);
    assertEquals(UnreadMessageHandler.class, messageHandlerUnreadMessages.getClass());

    MessageHandler messageHandlerGetAllGroupMod = jsonMessageHandlerFactory.getMessageHandler(MessageType.GET_ALL_GROUPS_MOD);
    assertEquals(GetAllGroupsModHandler.class, messageHandlerGetAllGroupMod.getClass());


    MessageHandler messageHandlerGetAllUsersForGroup = jsonMessageHandlerFactory.getMessageHandler(MessageType.GET_ALL_USERS_FOR_GRP);
    assertEquals(GetAllUsersForGroup.class, messageHandlerGetAllUsersForGroup.getClass());


    MessageHandler messageHandlerToggleMod = jsonMessageHandlerFactory.getMessageHandler(MessageType.TOGGLE_MODERATOR);
    assertEquals(ToggleModeratorRightsHandler.class, messageHandlerToggleMod.getClass());

    MessageHandler messageHandlerRenameGroup = jsonMessageHandlerFactory.getMessageHandler(MessageType.RENAME_GROUP);
    assertEquals(RenameGroupHandler.class, messageHandlerRenameGroup.getClass());


    MessageHandler messageHandlerDeleteGroup = jsonMessageHandlerFactory.getMessageHandler(MessageType.DELETE_GROUP);
    assertEquals(DeleteGroupHandler.class, messageHandlerDeleteGroup.getClass());

    MessageHandler messageHandlerGroupSearch = jsonMessageHandlerFactory.getMessageHandler(MessageType.GROUP_SEARCH);
    assertEquals(GroupSearchHandler.class, messageHandlerGroupSearch.getClass());

  }
}
