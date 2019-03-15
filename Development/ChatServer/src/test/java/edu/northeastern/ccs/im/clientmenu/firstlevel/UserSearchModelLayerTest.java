package edu.northeastern.ccs.im.clientmenu.firstlevel;

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
import edu.northeastern.ccs.im.clientmenu.models.UserSearch;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;

public class UserSearchModelLayerTest {

  @Mock
  private Connection connection;

  private Gson mGson;

  private UserSearchModelLayer userSearchModelLayer;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    userSearchModelLayer = new UserSearchModelLayer();
    mGson = new Gson();
  }

  @Test
  public void passControlTest() {
    String str = "atti\n" + "hello\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    UserSearch userSearch = new UserSearch("atti");
    List<String> userList = new ArrayList<>();
    userList.add("hello");
    userList.add("world");
    userSearch.setUsersList(userList);
    String userString = mGson.toJson(userSearch);
    MessageJson messageJson = new MessageJson("atti", MessageType.USER_SEARCH, userString);
    when(connection.next()).thenReturn(messageJson);
    userSearchModelLayer.passControl(scanner, connection);
  }

  @Test
  public void noUserNameReturnedTest() {
    String str = "atti\n" + "hello\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    UserSearch userSearch = new UserSearch("atti");
    List<String> userList = new ArrayList<>();
    userSearch.setUsersList(userList);
    String userString = mGson.toJson(userSearch);
    MessageJson messageJson = new MessageJson("atti", MessageType.USER_SEARCH, userString);
    when(connection.next()).thenReturn(messageJson);
    userSearchModelLayer.passControl(scanner, connection);
  }


  @Test
  public void doesNotContainUserToChatWithTest() {
    String str = "atti\n" + "hello\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    UserSearch userSearch = new UserSearch("atti");
    List<String> userList = new ArrayList<>();
    userList.add("world");
    userSearch.setUsersList(userList);
    String userString = mGson.toJson(userSearch);
    MessageJson messageJson = new MessageJson("atti", MessageType.USER_SEARCH, userString);
    when(connection.next()).thenReturn(messageJson);
    userSearchModelLayer.passControl(scanner, connection);
  }

  @Test
  public void listenTest() {
    String str = "atti\n" + "hello\n" +  "wpr\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    UserSearch userSearch = new UserSearch("atti");
    List<String> userList = new ArrayList<>();
    userList.add("world");
    userSearch.setUsersList(userList);
    String userString = mGson.toJson(userSearch);
    MessageJson messageJson = new MessageJson("atti", MessageType.USER_SEARCH, userString);
    when(connection.next()).thenReturn(messageJson);
    userSearchModelLayer.passControl(scanner, connection);
    userSearchModelLayer.listen(userString);
  }

  @Test
  public void listenEmptyUserNameListTest() {
    String str = "atti\n" + "hello\n" +  "wpr\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    UserSearch userSearch = new UserSearch("atti");
    List<String> userList = new ArrayList<>();
    userSearch.setUsersList(userList);
    String userString = mGson.toJson(userSearch);
    MessageJson messageJson = new MessageJson("atti", MessageType.USER_SEARCH, userString);
    when(connection.next()).thenReturn(messageJson);
    userSearchModelLayer.passControl(scanner, connection);
    userSearchModelLayer.listen(userString);
  }

}
