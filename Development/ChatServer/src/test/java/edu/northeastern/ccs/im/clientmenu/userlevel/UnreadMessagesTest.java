package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.UnreadMessageModel;

import static org.mockito.Mockito.when;

public class UnreadMessagesTest {


  private UnreadMessages unreadMessages;

  @Mock
  Connection connection;
  private Gson mGson;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    unreadMessages = new UnreadMessages();
    mGson = new Gson();
  }

  @Test
  public void userChatMessageUnreadMessagesTest() {

    String str = "";
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(byteArrayInputStream);

    UnreadMessageModel unreadMessageModel = new UnreadMessageModel("atti", "kumar", new Date(), false);

    List<UnreadMessageModel> unreadMessagesList = new ArrayList<>();
    unreadMessagesList.add(unreadMessageModel);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.UNREAD_MSG, mGson.toJson(unreadMessagesList));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);


    unreadMessages.passControl(scanner, connection);
  }

  @Test
  public void groupMessageUnreadMessagesTest() {

    String str = "";
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(byteArrayInputStream);

    UnreadMessageModel unreadMessageModel = new UnreadMessageModel("atti", "kumar", new Date(), true);

    List<UnreadMessageModel> unreadMessagesList = new ArrayList<>();
    unreadMessagesList.add(unreadMessageModel);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.UNREAD_MSG, mGson.toJson(unreadMessagesList));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);


    unreadMessages.passControl(scanner, connection);
  }

  @Test
  public void blankResponseUnreadMessagesTest() {
    String str = "";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.UNREAD_MSG, "");

    when(connection.next()).thenReturn(response);

    unreadMessages.passControl(scanner, connection);

  }
}
