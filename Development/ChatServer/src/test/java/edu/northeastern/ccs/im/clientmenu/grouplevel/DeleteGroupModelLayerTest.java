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

public class DeleteGroupModelLayerTest {

  private final static String USERNAME = "atti\n";
  private final static String QUIT = "\\q\n";
  private Gson gson;
  private DeleteGroupModelLayer deleteGroupModelLayer;

  @Mock
  Connection connection;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    deleteGroupModelLayer = new DeleteGroupModelLayer();
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
    deleteGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void emptyListTest() {
    String str =  USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    deleteGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void deleteGroupErrorTest() {
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
    deleteGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void deleteGroupDontHaveRightTest() {
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
    deleteGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void successfullyDeletedGroupTest() {
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
    deleteGroupModelLayer.passControl(scanner, connection);

  }
}
