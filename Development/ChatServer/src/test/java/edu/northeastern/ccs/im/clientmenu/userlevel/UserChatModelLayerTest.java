package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.ChatModel;

import static org.mockito.Mockito.when;

public class UserChatModelLayerTest {

  private static final String USERNAME = "atti\n";
  private static final String QUIT = "\\q\n";

  @Mock
  private Connection connection;

  private Gson mGson;


  @Mock
  private ChatModel userChat;

  private UserChatModelLayer userChatModelLayer;



  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    userChatModelLayer = new UserChatModelLayer("atti");
    mGson = new Gson();
  }

  @Test
  public void passControlTest() {
    String str =  USERNAME + USERNAME + QUIT;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    AckModel ackModel = new AckModel();
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, mGson.toJson(ackModel));
    when(connection.next()).thenReturn(response);
    userChatModelLayer.passControl(scanner,connection);
  }

  @Test
  public void groupChatTest() {
    String str =  USERNAME + USERNAME + QUIT;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    AckModel ackModel = new AckModel();
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, mGson.toJson(ackModel));
    when(connection.next()).thenReturn(response);
    userChatModelLayer.passControl(scanner,connection);
    ChatModel chatModel = new ChatModel();
    chatModel.setMsg("message");
    MessageJson messageJson1 = new MessageJson("msg", MessageType.GROUP_CHAT, mGson.toJson(chatModel));
    userChatModelLayer.displayResponse(messageJson1);
  }

  @Test
  public void userChatTest() {
    String str =  USERNAME + USERNAME + QUIT;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    AckModel ackModel = new AckModel();
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, mGson.toJson(ackModel));
    when(connection.next()).thenReturn(response);
    userChatModelLayer.passControl(scanner,connection);
    ChatModel chatModel = new ChatModel();
    chatModel.setMsg("message");
    MessageJson messageJson1 = new MessageJson("msg", MessageType.USER_CHAT, mGson.toJson(chatModel));
    userChatModelLayer.displayResponse(messageJson1);
  }



  @Test
  public void scannerNoHasNextTest() {
    String str =  "";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    userChatModelLayer.passControl(scanner,connection);

  }

  @Test
  public void displayResponseNullTest() {
    userChatModelLayer.displayResponse(null);
  }
}
