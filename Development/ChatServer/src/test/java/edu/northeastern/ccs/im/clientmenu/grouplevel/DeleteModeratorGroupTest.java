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

public class DeleteModeratorGroupTest {

  private final static String USERNAME = "atti\n";
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
    DeleteModeratorGroup deleteModeratorGroup = new DeleteModeratorGroup();
    deleteModeratorGroup.passControl(scanner, connection);

  }

  @Test
  public void containsKeyAndIsModeratorTest() {
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
    DeleteModeratorGroup deleteModeratorGroup = new DeleteModeratorGroup();
    deleteModeratorGroup.passControl(scanner, connection);

  }

  @Test
  public void containsKeyModeratorTest() {
    String str =  USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", true);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    MessageJson response1 = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.TOGGLE_MODERATOR, gson.toJson(true));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response).thenReturn(response1);
    DeleteModeratorGroup deleteModeratorGroup = new DeleteModeratorGroup();
    deleteModeratorGroup.passControl(scanner, connection);

  }

  @Test
  public void containsKeyButNotModeratorTest() {
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
    DeleteModeratorGroup deleteModeratorGroup = new DeleteModeratorGroup();
    deleteModeratorGroup.passControl(scanner, connection);

  }
}
