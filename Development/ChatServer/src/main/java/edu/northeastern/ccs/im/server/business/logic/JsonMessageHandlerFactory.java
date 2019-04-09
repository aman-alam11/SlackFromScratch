package edu.northeastern.ccs.im.server.business.logic;

import edu.northeastern.ccs.im.clientmenu.superuser.SuperUser;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.business.logic.handler.AddGroupUsersHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.ChatHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.DeleteGroupHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.GetAllGroupsModHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.GetAllUsersForGroup;
import edu.northeastern.ccs.im.server.business.logic.handler.GroupChatHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.GroupCreationHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.GroupSearchHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.LoginHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.RecallUserChatHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.RenameGroupHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.SuperUserHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.ToggleModeratorRightsHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.TransLateHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.UnreadMessageHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.UserCreationHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.UserSearchHandler;

/**
 * MessageHandler Factory which returns new instance of MessageHandler depending on the MessageType.
 * MessageType is an enum.
 */
public class JsonMessageHandlerFactory implements MessageHandlerFactory {

  @Override
  public MessageHandler getMessageHandler(MessageType msgType) {
    MessageHandler handler = null;
    switch (msgType) {

      case CREATE_USER:
        handler = new UserCreationHandler();
        break;

      case LOGIN:
        handler = new LoginHandler();
        break;

      case USER_CHAT:
        handler = new ChatHandler();
        break;
       
      case USER_SEARCH:
      	handler = new UserSearchHandler();
      	break;

      case CREATE_GROUP:
        handler = new GroupCreationHandler();
        break;

      case GROUP_CHAT:
        handler = new GroupChatHandler();
        break;

      case ADD_USER_IN_GROUP:
      	handler = new AddGroupUsersHandler();
      	break;
        
      case UNREAD_MSG:
        handler = new UnreadMessageHandler();
        break;

      case GET_ALL_GROUPS_MOD:
        handler = new GetAllGroupsModHandler();
        break;

      case GET_ALL_USERS_FOR_GRP:
        handler = new GetAllUsersForGroup();
        break;

      case TOGGLE_MODERATOR:
        handler = new ToggleModeratorRightsHandler();
        break;


      case RENAME_GROUP:
        handler = new RenameGroupHandler();
        break;

      case DELETE_GROUP:
        handler = new DeleteGroupHandler();
        break;

      case GROUP_SEARCH:
      	handler = new GroupSearchHandler();
      	break;

      case DELETER_USER_FROM_GROUP:
        handler = new ToggleModeratorRightsHandler();
        break;

      case CHAT_RECALL:
        handler = new RecallUserChatHandler();
        break;

      case SUPER_USER:
        handler = new SuperUserHandler();
        break;

      case FOLLOW_USER:
        handler = new AddFollowerUserHandler();
        break;

      case LIST_FOLLOWERS:
        handler = new ListFollowerUserHandler();
        break;
      case TRANSLATE_MESSAGE:
        handler = new TransLateHandler();
        break;

      default:
        // Send to Login Page as default for safety
        handler = new LoginHandler();
        break;
    }
    return handler;
  }

}
