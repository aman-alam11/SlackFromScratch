package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.GroupSearchModel;

import static org.mockito.Mockito.when;

public class GroupSearchModelLayerTest {

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
    String str =  USERNAME + QUIT;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    when(connection.hasNext()).thenReturn(true);
    GroupSearchModel searchModel = new GroupSearchModel(USERNAME);
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, gson.toJson(searchModel));
    when(connection.next()).thenReturn(response);
    GroupSearchModelLayer createGroupModelLayer = new GroupSearchModelLayer();
    createGroupModelLayer.passControl(scanner,connection);
  }

  @Test
  public void searchResultListEmptyTest() {
    String str =  USERNAME + USERNAME + QUIT;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    GroupSearchModel searchModel = new GroupSearchModel(USERNAME);
    List<String> list = new ArrayList<>();
    list.add("atti");
    searchModel.setGroupList(list);
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, gson.toJson(searchModel));
    when(connection.next()).thenReturn(response);
    GroupSearchModelLayer createGroupModelLayer = new GroupSearchModelLayer();
    createGroupModelLayer.passControl(scanner, connection);
  }

}
