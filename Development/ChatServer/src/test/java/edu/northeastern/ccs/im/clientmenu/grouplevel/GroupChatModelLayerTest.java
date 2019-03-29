package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.ChatModel;

import static org.mockito.Mockito.when;

public class GroupChatModelLayerTest {

  private final static String USERNAME = "atti\n";
  private final static String QUIT = "\\q\n";
  private Gson gson;

  @Mock
  Connection connection;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    gson = new Gson();
  }

  @Test
  public void passControlTest() {
    String str =  USERNAME + USERNAME + QUIT;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    AckModel ackModel = new AckModel();
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, gson.toJson(ackModel));
    when(connection.next()).thenReturn(response);
    GroupChatModelLayer groupChatModelLayer = new GroupChatModelLayer(USERNAME);
    groupChatModelLayer.passControl(scanner,connection);
    ChatModel chatModel = new ChatModel();
    chatModel.setMsg("message");
    MessageJson messageJson = new MessageJson("msg", MessageType.USER_CHAT, gson.toJson(chatModel));
    groupChatModelLayer.displayResponse(messageJson);

    ChatModel chatModel1 = new ChatModel();
    chatModel.setMsg("message");
    MessageJson messageJson1 = new MessageJson("msg",MessageType.GROUP_CHAT, gson.toJson(chatModel1));
    groupChatModelLayer.displayResponse(messageJson1);
  }

  @Test
  public void displayResponseNullTest() {

    GroupChatModelLayer groupChatModelLayer = new GroupChatModelLayer("name");
    groupChatModelLayer.displayResponse(null);
  }


}
