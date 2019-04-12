package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class DeleteUsersFromGroupTest {

  private ByteArrayOutputStream mOutputStream;
  private Scanner mScanner;

  @Mock
  Connection connection;

  private DeleteUsersFromGroup mDeleteUsersFromGroup;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);

    mDeleteUsersFromGroup = new DeleteUsersFromGroup();
    mOutputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(mOutputStream));
  }

  @After
  public void deInit() {
    System.setOut(System.out);
    System.setIn(System.in);
  }

  private void utilScanner(String dummyString) {
    InputStream mByteArrayInputStream = new ByteArrayInputStream(dummyString.getBytes());
    mScanner = new Scanner(mByteArrayInputStream);
    System.setIn(mByteArrayInputStream);
  }


  @Test
  public void testEmptyList() {
    utilScanner("1"
            + System.lineSeparator() + "atti"
            + System.lineSeparator() + "1"
            + System.lineSeparator() + "");

    Map<String, Boolean> userMap = new HashMap<>();
    MessageJson messageJson = new MessageJson("atti", MessageType.DELETER_USER_FROM_GROUP, new Gson().toJson(userMap));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mDeleteUsersFromGroup.passControl(mScanner, connection);
    assertEquals("INFO: Getting all users for group: \n" +
            "\n" +
            "DONE\n" +
            "\n" +
            "ERROR: Only 0 member left in group.Can't delete users for now.\n" +
            " Taking you back\n" +
            "\n" +
            "1. Create Group\n" +
            "2. Update Group\n" +
            "3. Group Chat\n" +
            "4. Delete Group\n" +
            "Or Enter \\b to go back or \\q to quit\n" +
            "INPUT: Enter From above Options: \n", mOutputStream.toString());
  }


  @Test
  public void testListWithUsers() {
    utilScanner("1"
            + System.lineSeparator() + "atti"
            + System.lineSeparator() + "1"
            + System.lineSeparator() + "");

    Map<String, Boolean> userMap = new HashMap<>();
    userMap.put("one", true);
    userMap.put("one1", true);
    userMap.put("one2", true);
    userMap.put("one3", true);

    MessageJson messageJson = new MessageJson("atti", MessageType.DELETER_USER_FROM_GROUP, new Gson().toJson(userMap));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mDeleteUsersFromGroup.passControl(mScanner, connection);
    assertEquals("INFO: Getting all users for group: \n" +
            "\n" +
            "DONE\n" +
            "\n" +
            "USER: one Already a moderator: Yes\n" +
            "USER: one2 Already a moderator: Yes\n" +
            "USER: one3 Already a moderator: Yes\n" +
            "USER: one1 Already a moderator: Yes\n" +
            "INPUT: Which user from above do you want to remove from the group?\n" +
            "ERROR: Illegal Name Entered. Sending you back\n" +
            "1. Create Group\n" +
            "2. Update Group\n" +
            "3. Group Chat\n" +
            "4. Delete Group\n" +
            "Or Enter \\b to go back or \\q to quit\n" +
            "INPUT: Enter From above Options: \n", mOutputStream.toString());
  }

  @Test
  public void testListWithUsers2() {
    utilScanner("one1");

    Map<String, Boolean> userMap = new HashMap<>();
    userMap.put("one", true);
    userMap.put("one1", false);
    userMap.put("one2", true);
    userMap.put("one3", true);

    MessageJson messageJson = new MessageJson("atti", MessageType.DELETER_USER_FROM_GROUP, new Gson().toJson(userMap));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mDeleteUsersFromGroup.passControl(mScanner, connection);
    assertEquals("INFO: Getting all users for group: \n" +
            "\n" +
            "DONE\n" +
            "\n" +
            "USER: one Already a moderator: Yes\n" +
            "USER: one2 Already a moderator: Yes\n" +
            "USER: one3 Already a moderator: Yes\n" +
            "USER: one1 Already a moderator: No\n" +
            "INPUT: Which user from above do you want to remove from the group?\n" +
            "\n" +
            "DONE\n" +
            "\n" +
            "ERROR: Operation Failed. Please try again\n" +
            "1. Create Group\n" +
            "2. Update Group\n" +
            "3. Group Chat\n" +
            "4. Delete Group\n" +
            "Or Enter \\b to go back or \\q to quit\n" +
            "INPUT: Enter From above Options: \n", mOutputStream.toString());
  }


  @Test
  public void getAllGroupsUtilTest() {

    Map<String, Boolean> stringBooleanMap =  new HashMap<>();

    MessageJson messageJsonState = new MessageJson("atti", MessageType.GET_ALL_GROUPS_MOD, new Gson().toJson(stringBooleanMap));
    MessageJson messageJson = new MessageJson("atti", MessageType.GET_ALL_GROUPS_MOD,
            new Gson().toJson(messageJsonState));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

      assertEquals("{messageType=false, fromUser=false, creationTime=false, message=false}",
              GetAllGroupsUtil.parseResponse(MessageType.GET_ALL_GROUPS_MOD, connection).toString());
  }


}
