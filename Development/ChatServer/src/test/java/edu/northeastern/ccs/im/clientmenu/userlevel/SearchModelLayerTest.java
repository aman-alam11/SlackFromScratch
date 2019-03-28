package edu.northeastern.ccs.im.clientmenu.userlevel;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static org.mockito.Mockito.when;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.models.Search;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;

public class SearchModelLayerTest {

  @Mock
  private Connection connection;

  private Gson mGson;

  private UserSearchModelLayer userSearchModelLayer;

  private static final String USERNAME = "atti\n";
  private static final String HELLO = "hello\n";
  private static final String WORLD = "world\n";


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    userSearchModelLayer = new UserSearchModelLayer();
    mGson = new Gson();
  }

  @Test
  public void passControlTest() {
    String str = USERNAME + HELLO;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    Search search = new Search("atti");
    List<String> userList = new ArrayList<>();
    userList.add("hello");
    userList.add(WORLD);
    search.setList(userList);
    String userString = mGson.toJson(search);
    MessageJson messageJson = new MessageJson("atti", MessageType.USER_SEARCH, userString);

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);
    userSearchModelLayer.passControl(scanner, connection);
  }


  @Test
  public void noUsersReturnedTest() {
    String str = USERNAME + HELLO;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    Search search = new Search("atti");
    List<String> userList = new ArrayList<>();
    search.setList(userList);
    String userString = mGson.toJson(search);
    MessageJson messageJson = new MessageJson("atti", MessageType.USER_SEARCH, userString);

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);
    userSearchModelLayer.passControl(scanner, connection);
  }


  @Test
  public void wrongUserEnteredTest() {
    String str = "atti\n" + "pass\n" + "lol\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    Search search = new Search("atti");
    List<String> userList = new ArrayList<>();
    userList.add("hello");
    search.setList(userList);
    String userString = mGson.toJson(search);
    MessageJson messageJson = new MessageJson("atti", MessageType.USER_SEARCH, userString);

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);
    userSearchModelLayer.passControl(scanner, connection);
  }

  @Test
  public void blankResponseUserSearchTest() {
    String str = "atti\n" + "pass\n" + "lol\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            "");

    when(connection.next()).thenReturn(responsePacket);

    userSearchModelLayer.passControl(scanner, connection);

  }


}
