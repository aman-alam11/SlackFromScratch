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
import edu.northeastern.ccs.im.model.GroupSearchModel;

import static org.mockito.Mockito.when;

public class UpdateGroupModelLayerTest {

  private final static String USERNAME = "atti\n";
  private final static String QUIT = "\\q\n";
  private Gson gson;
  private UpdateGroupModelLayer updateGroupModelLayer;

  @Mock
  Connection connection;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    updateGroupModelLayer = new UpdateGroupModelLayer();
    gson = new Gson();
  }

  @Test
  public void passControlTest() {
    String str =  USERNAME + "1\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", true);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    updateGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void notAModeratorTest() {
    String str =  USERNAME + "1\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", false);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    updateGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void choiceTwoTest() {
    String str =  USERNAME + "2\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", true);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    updateGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void choiceThreeTest() {
    String str =  USERNAME + "3\n"+ "2\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", true);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    updateGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void noGroupsMatchedTest() {
    String str =  USERNAME + "3\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti1", true);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    updateGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void emptyListTest() {
    String str =  USERNAME + "3\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    updateGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void fourthOptionTest() {
    String str =  USERNAME + "4\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", true);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    updateGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void newNameTest() {
    String str =  USERNAME + "3\n" + USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);



    Map<String, Boolean> listAllGroupsForUser = new HashMap<>();
    listAllGroupsForUser.put("atti", true);
    listAllGroupsForUser.put("atti2", true);

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.GET_ALL_GROUPS_MOD, gson.toJson(listAllGroupsForUser));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);
    updateGroupModelLayer.passControl(scanner, connection);

  }

  @Test
  public void renamedGroupTest() {
    String str =  USERNAME + "3\n" + "hello";
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
    updateGroupModelLayer.passControl(scanner, connection);

  }
}
