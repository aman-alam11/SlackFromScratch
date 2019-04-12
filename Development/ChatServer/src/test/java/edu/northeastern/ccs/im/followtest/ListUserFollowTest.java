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
import edu.northeastern.ccs.im.clientmenu.userlevel.ListUserFollowerLayer;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.User;
import edu.northeastern.ccs.im.model.UserFollwingList;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class ListUserFollowTest {

  private ListUserFollowerLayer mListUserFollowLayer;
  private ByteArrayOutputStream mOutputStream;

  @Mock
  Connection connection;
  private Scanner mScanner;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);

    mListUserFollowLayer = new ListUserFollowerLayer();
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
  public void test(){
    utilScanner("1");

    List<User> userList = new ArrayList<>();
    userList.add(new User("firstName", true));
    userList.add(new User("secondName", true));
    userList.add(new User("thirdName", true));
    UserFollwingList userFollwingList = new UserFollwingList(userList);


    MessageJson messageJson = new MessageJson("atti", MessageType.LIST_FOLLOWERS,
            new Gson().toJson(userFollwingList));

    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mListUserFollowLayer.passControl(mScanner, connection);

    assertEquals("\n" +
            "DONE\n" +
            "\n" +
            "RESULTS: Following are your followers.\n" +
            "1: firstName --- Online\n" +
            "2: secondName --- Online\n" +
            "3: thirdName --- Online\n" +
            "===================\n" +
            "1. Follow a User\n" +
            "2. List my followers\n" +
            "Or Enter \\b to go back or \\q to quit\n" +
            "INPUT: Enter From above Options: \n", mOutputStream.toString());
  }
}
