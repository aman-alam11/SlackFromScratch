package edu.northeastern.ccs.im.followtest;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.userlevel.CreateUserFollowLayer;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.UserFollow;
import edu.northeastern.ccs.im.model.UserSearch;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class CreateUserFollowTest {

  private CreateUserFollowLayer mCreateUserFollowLayer;
  private ByteArrayOutputStream mOutputStream;

  @Mock
  Connection connection;
  private Scanner mScanner;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);

    mCreateUserFollowLayer = new CreateUserFollowLayer();
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
  public void testEmptyUserList() {
    utilScanner("1");

    UserSearch userSearch = new UserSearch("atti");
    List<String> usersList = new ArrayList<>();
    userSearch.setUsersList(usersList);

    MessageJson messageJson = new MessageJson("kumar", MessageType.USER_SEARCH
            , new Gson().toJson(userSearch));


    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mCreateUserFollowLayer.passControl(mScanner, connection);
    assertEquals("INPUT: Enter User name you want to follow: " + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "No Users Found" + System.lineSeparator() +
            "1. Follow a User" + System.lineSeparator() +
            "2. List my followers" + System.lineSeparator() +
            "Or Enter \\b to go back or \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());

  }

  @Test
  public void testUserListWithUsers() {
    utilScanner("1" + System.lineSeparator() + "att");

    UserSearch userSearch = new UserSearch("atti");
    List<String> usersList = new ArrayList<>();
    usersList.add("att");
    usersList.add("at");
    usersList.add("atti1");
    userSearch.setUsersList(usersList);

    MessageJson messageJson = new MessageJson("kumar", MessageType.USER_SEARCH
            , new Gson().toJson(userSearch));


    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mCreateUserFollowLayer.passControl(mScanner, connection);
    assertEquals("INPUT: Enter User name you want to follow: " + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "att" + System.lineSeparator() +
            "at" + System.lineSeparator() +
            "atti1" + System.lineSeparator() +
            "INPUT: Which user from above do you want to follow?" + System.lineSeparator() +
            "Oops! Something bad happened while adding you as a follower of att" + System.lineSeparator() +
            "1. Follow a User" + System.lineSeparator() +
            "2. List my followers" + System.lineSeparator() +
            "Or Enter \\b to go back or \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
  }

  @Test
  public void testUserListWithUsersIllegalEntry() {
    utilScanner("1" + System.lineSeparator() + "invalidUser");

    UserSearch userSearch = new UserSearch("atti");
    List<String> usersList = new ArrayList<>();
    usersList.add("att");
    usersList.add("at");
    usersList.add("atti1");
    userSearch.setUsersList(usersList);

    MessageJson messageJson = new MessageJson("kumar", MessageType.USER_SEARCH
            , new Gson().toJson(userSearch));


    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mCreateUserFollowLayer.passControl(mScanner, connection);
    assertEquals("INPUT: Enter User name you want to follow: " + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "att" + System.lineSeparator() +
            "at" + System.lineSeparator() +
            "atti1" + System.lineSeparator() +
            "INPUT: Which user from above do you want to follow?" + System.lineSeparator() +
            "ERROR: Illegal Name Entered. Sending you back" + System.lineSeparator() +
            "1. Follow a User" + System.lineSeparator() +
            "2. List my followers" + System.lineSeparator() +
            "Or Enter \\b to go back or \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());

  }

  @Test
  public void testUserListWithUsersSuccess() {
    utilScanner("1" + System.lineSeparator() + "at");
    Gson gson = new Gson();

    UserSearch userSearch = new UserSearch("atti");
    List<String> usersList = new ArrayList<>();
    usersList.add("att");
    usersList.add("at");
    usersList.add("atti1");
    userSearch.setUsersList(usersList);

    MessageJson messageJson = new MessageJson("kumar", MessageType.USER_SEARCH
            , gson.toJson(userSearch));

    UserFollow userFollow = new UserFollow("at", "kumar");

    MessageJson messageJsonFollow = new MessageJson("kumar", MessageType.FOLLOW_USER,
            gson.toJson(userFollow));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    when(connection.sendMessage(messageJsonFollow)).thenReturn(true);
    mCreateUserFollowLayer.passControl(mScanner, connection);

    assertEquals("INPUT: Enter User name you want to follow: " + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "att" + System.lineSeparator() +
            "at" + System.lineSeparator() +
            "atti1" + System.lineSeparator() +
            "INPUT: Which user from above do you want to follow?" + System.lineSeparator() +
            "Oops! Something bad happened while adding you as a follower of at" + System.lineSeparator() +
            "1. Follow a User" + System.lineSeparator() +
            "2. List my followers" + System.lineSeparator() +
            "Or Enter \\b to go back or \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());

  }

}
