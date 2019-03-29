package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;

import static org.mockito.Mockito.when;

public class AddModeratorGroupTest {

  private final static String USERNAME = "atti\n";
  private final static String QUIT = "\\q\n";
  private Gson gson;
  private AddModeratorGroup addModeratorGroup;

  @Mock
  Connection connection;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    addModeratorGroup = new AddModeratorGroup();
    gson = new Gson();
  }


  @Test
  public void passControlTest() {
    String str =  USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti1", false);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    addModeratorGroup.passControl(scanner, connection);

  }

  @Test
  public void userContainsKeyTest() {
    String str =  USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", true);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    addModeratorGroup.passControl(scanner, connection);

  }

  @Test
  public void notAModeratorTest() {
    String str =  USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", false);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));




    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    addModeratorGroup.passControl(scanner, connection);

    GroupLayer groupLayer = new GroupLayer();
    groupLayer.passControl(scanner, connection);

  }

  @Test
  public void toggleTest() {
    String str =  USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", false);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));


    MessageJson response1 = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.TOGGLE_MODERATOR, gson.toJson(true));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response).thenReturn(response1);

    addModeratorGroup.passControl(scanner, connection);

  }
}
