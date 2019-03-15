package edu.northeastern.ccs.im.clientmenu.firstlevel;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.models.UserChat;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import static org.mockito.Mockito.when;

public class UserChatModelLayerTest {

  private static final String USERNAME = "atti\n";
  private static final String QUIT = "\\q\n";

  @Mock
  private Connection connection;

  private Gson mGson;


  @Mock
  private UserChat userChat;

  private UserChatModelLayer userChatModelLayer;



  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    userChatModelLayer = new UserChatModelLayer("atti");
    mGson = new Gson();
  }

  @Test
  public void passControlTest() {
    String str = "1\n" + USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    userChatModelLayer.listen("message");
    userChatModelLayer.passControl(scanner, connection);
  }

  @Test
  public void passControlQuitTest() {
    String str = QUIT;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    userChatModelLayer.passControl(scanner, connection);
  }

  @Test
  public void frameChatMessageToDisplayTest() {
    when(userChat.getMsg()).thenReturn("message from user");
    String str = userChatModelLayer.frameChatMessageToDisplay(userChat, MessageType.USER_CHAT);

    assertEquals("USER_CHAT-userChat",str);
  }

  @Test
  public void runTest() {
    String str = "1\n" + USERNAME + QUIT;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    userChatModelLayer.passControl(scanner, connection);
    userChatModelLayer.run();
  }

  @Test
  public void run2Test() {
    String str = "1\n" + USERNAME + QUIT;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    UserChat userChat1 = new UserChat();
    userChat1.setMsg("hello");
    userChat1.setFromUserName("atti");

    String message = mGson.toJson(userChat1);
    MessageJson messageJson = new MessageJson("atti",MessageType.USER_CHAT,message);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);
    userChatModelLayer.passControl(scanner, connection);
    userChatModelLayer.run();
  }
}
