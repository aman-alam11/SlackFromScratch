package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.GroupChatHandler;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class GroupChatHandlerTest {

  private GroupChatHandler groupChatHandler;

  @Mock
  private Connection connection;

  @Mock
  private JPAService jpaService;

  @Mock
  private User user;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    JPAService.setJPAService(jpaService);
    groupChatHandler = new GroupChatHandler();
  }

  @Test
  public void handleMessageTrueTest() {
    ChatModel chatModel = new ChatModel();
    List<User> users = new ArrayList<>();
    users.add(user);
    when(user.getName()).thenReturn("str");
    when(JPAService.getInstance().findAllMembersOfGroup(any())).thenReturn(users);
    boolean b =  groupChatHandler.handleMessage("str",new Gson().toJson(chatModel),connection);
    assertTrue(b);
  }

  @Test
  public void handleMessageNameNotEqualTest() {
    ChatModel chatModel = new ChatModel();
    List<User> users = new ArrayList<>();
    users.add(user);
    when(user.getName()).thenReturn("tr");
    when(JPAService.getInstance().findAllMembersOfGroup(any())).thenReturn(users);
    boolean b =  groupChatHandler.handleMessage("str",new Gson().toJson(chatModel),connection);
    assertTrue(b);
  }
}
