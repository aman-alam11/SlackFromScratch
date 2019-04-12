package edu.northeastern.ccs.im.clientmenu;

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
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.superuser.SuperUser;
import edu.northeastern.ccs.im.clientmenu.userlevel.UnreadMessages;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.ErrorCodes;
import edu.northeastern.ccs.im.model.UnreadMessageModel;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class SuperUserClientTest {

  private SuperUser mSuperUser;
  private ByteArrayOutputStream mOutputStream;
  private Scanner mScanner;

  @Mock
  Connection connection;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);

    mSuperUser = new SuperUser();
    mOutputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(mOutputStream));
  }

  @After
  public void deInit() {
    System.setOut(System.out);
    System.setIn(System.in);
  }

  @Test(expected = NullPointerException.class)
  public void testNullInit() {
    mSuperUser.passControl(null, null);
  }


  private void utilScanner(String dummyString) {
    InputStream mByteArrayInputStream = new ByteArrayInputStream(dummyString.getBytes());
    mScanner = new Scanner(mByteArrayInputStream);
    System.setIn(mByteArrayInputStream);
  }

  @Test
  public void testCaseUserChatsNoDates() {

    utilScanner("1"
            + System.lineSeparator() + "atti"
            + System.lineSeparator() + "1"
            + System.lineSeparator() + "");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group" + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the name of user to tap into his/her user2user chats:" + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get all " +
            "dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Uh Oh! This is embarrassing. Something went wrong." + System.lineSeparator() +
            " Sending you back to main menu" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();

    // blank name branch
    utilScanner("1"
            + System.lineSeparator() + "" + System.lineSeparator());

    messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);
    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group" + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the name of user to tap into his/her user2user chats:" + System.lineSeparator() +
            "Input can't be blank. Sending you back" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
  }

  @Test
  public void testCaseGetGroupChatsNoDates() {
    utilScanner("2"
            + System.lineSeparator() + "atti"
            + System.lineSeparator() + "1"
            + System.lineSeparator() + "");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group" + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the name of user to tap into his/her group2group chats:" + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get" +
            " all dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Uh Oh! This is embarrassing. Something went wrong." + System.lineSeparator() +
            " Sending you back to main menu" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }

  @Test
  public void testCaseGetUserAndGroupNoDates() {
    utilScanner("3"
            + System.lineSeparator() + "atti"
            + System.lineSeparator() + "1"
            + System.lineSeparator() + "");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats"
            + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group" + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the name of user to tap into his/her user and group chats:" + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want " +
            "to get all dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Uh Oh! This is embarrassing. Something went wrong." + System.lineSeparator() +
            " Sending you back to main menu" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }

  @Test
  public void testCaseOnlyGroupByNameNoDates() {
    utilScanner("4"
            + System.lineSeparator() + "team212"
            + System.lineSeparator() + "1"
            + System.lineSeparator() + "");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats"
            + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group" + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the group name for which you want to get all the conversations from:"
            + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get all dates" +
            " of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames"
            + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Uh Oh! This is embarrassing. Something went wrong." + System.lineSeparator() +
            " Sending you back to main menu" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }

  @Test
  public void testIllegalInput() {
    utilScanner("10");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group"
            + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Wrong option selected. Sending you back" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }

  @Test
  public void testCaseOnlyGroupByNameWithDates() {
    utilScanner("4"
            + System.lineSeparator() + "team212"
            + System.lineSeparator() + "2"
            + System.lineSeparator() + "04/04/2019"
            + System.lineSeparator() + "04/09/2019");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group"
            + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the group name for which you want to get all the conversations from:" + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get" +
            " all dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames" + System.lineSeparator() +
            "Dates entered should be of type: MM/DD/YYYY . You have to include \\/ in the date." +
            " 2 things to note: " + System.lineSeparator() +
            " 1) The start date is inclusive and end date is exclusive " + System.lineSeparator() +
            " 2) Adding invalid dates lead to removal of date criteria." + System.lineSeparator() +
            "Please enter the start date to get chats:" + System.lineSeparator() +
            "Please enter the end date to get chats:" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Uh Oh! This is embarrassing. Something went wrong." + System.lineSeparator() +
            " Sending you back to main menu" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }

  @Test
  public void testCaseOnlyGroupByNameWithDatesInvalidDate1() {
    utilScanner("4"
            + System.lineSeparator() + "team212"
            + System.lineSeparator() + "2"
            + System.lineSeparator() + "04/04/2019"
            + System.lineSeparator() + "02/02/2018");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group"
            + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the group name for which you want to get all the conversations from:"
            + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get all " +
            "dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames" + System.lineSeparator() +
            "Dates entered should be of type: MM/DD/YYYY . You have to include \\/ in the date." +
            " 2 things to note: " + System.lineSeparator() +
            " 1) The start date is inclusive and end date is exclusive " + System.lineSeparator() +
            " 2) Adding invalid dates lead to removal of date criteria." + System.lineSeparator() +
            "Please enter the start date to get chats:" + System.lineSeparator() +
            "Please enter the end date to get chats:" + System.lineSeparator() +
            "Invalid date entered. Getting all chats of requested type" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Uh Oh! This is embarrassing. Something went wrong." + System.lineSeparator() +
            " Sending you back to main menu" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }


  @Test
  public void testCaseOnlyGroupByNameWithDatesInvalidDate2() {
    utilScanner("4"
            + System.lineSeparator() + "team212"
            + System.lineSeparator() + "2"
            + System.lineSeparator() + "04/04/2019"
            + System.lineSeparator() + "0awkdl/18ajwd/2019");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats"
            + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group"
            + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the group name for which you want to get all the conversations from:"
            + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get all" +
            " dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames"
            + System.lineSeparator() +
            "Dates entered should be of type: MM/DD/YYYY . You have to include \\/ in the date." +
            " 2 things to note: " + System.lineSeparator() +
            " 1) The start date is inclusive and end date is exclusive " + System.lineSeparator() +
            " 2) Adding invalid dates lead to removal of date criteria." + System.lineSeparator() +
            "Please enter the start date to get chats:" + System.lineSeparator() +
            "Please enter the end date to get chats:" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Uh Oh! This is embarrassing. Something went wrong." + System.lineSeparator() +
            " Sending you back to main menu" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }

  @Test
  public void testCaseOnlyGroupByNameDatesInvalidChoice() {
    utilScanner("4"
            + System.lineSeparator() + "team212"
            + System.lineSeparator() + "10");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group"
            + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the group name for which you want to get all the conversations from:"
            + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get " +
            "all dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames"
            + System.lineSeparator() +
            "Invalid input. Getting all chats as default." + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Uh Oh! This is embarrassing. Something went wrong." + System.lineSeparator() +
            " Sending you back to main menu" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }


  @Test
  public void testCaseGetGroupChatsNoDatesWithEmptyResponse() {
    utilScanner("2"
            + System.lineSeparator() + "atti"
            + System.lineSeparator() + "1"
            + System.lineSeparator() + "");

    List<UnreadMessages> unreadMessageList = new ArrayList<>();
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.SUPER_USER,
            new Gson().toJson(unreadMessageList));
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats"
            + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group"
            + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the name of user to tap into his/her group2group chats:" + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get all" +
            " dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames"
            + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "No such results found. Sending you back" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }

  @Test
  public void testCaseGetGroupChatsDatesWithFilledResponse() {
    utilScanner("2"
            + System.lineSeparator() + "atti"
            + System.lineSeparator() + "1"
            + System.lineSeparator() + "");

    List<UnreadMessageModel> unreadMessageList = new ArrayList<>();
    Date date = new Date();
    unreadMessageList.add(new UnreadMessageModel("FROM 1st Person",
            " This is my message", date, false));
    unreadMessageList.add(new UnreadMessageModel("FROM 2nd Person",
            " This is my message2", date, true));
    unreadMessageList.add(new UnreadMessageModel("FROM 3rd Person",
            " This is my message3", date, false));

    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.SUPER_USER,
            new Gson().toJson(unreadMessageList));
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group"
            + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the name of user to tap into his/her group2group chats:" + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get " +
            "all dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames"
            + System.lineSeparator() + "" + System.lineSeparator() + "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Conversations for a particular User for all group chats" + System.lineSeparator() +
            "FROM: FROM 1st Person TIME: " + date + " MESSAGE:  This is my message"
            + System.lineSeparator() +
            "FROM: FROM 2nd Person TIME: " + date + " MESSAGE:  This is my message2"
            + System.lineSeparator() +
            "FROM: FROM 3rd Person TIME: " + date + " MESSAGE:  This is my message3"
            + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }

  @Test
  public void testCaseGetGroupChatsWithAckResponse() {
    utilScanner("2"
            + System.lineSeparator() + "atti"
            + System.lineSeparator() + "1"
            + System.lineSeparator() + ""
            + System.lineSeparator());

    AckModel ackModel = new AckModel();
    ackModel.addErrorCode(ErrorCodes.IL001);
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.SUPER_USER,
            new Gson().toJson(ackModel));
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(response);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group"
            + System.lineSeparator() +
            "4. Get All Conversations for a particular Group" + System.lineSeparator() +
            "Enter the name of user to tap into his/her group2group chats:" + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get all " +
            "dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "" + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }


  @Test
  public void invalidDates() {
    utilScanner("4"
            + System.lineSeparator() + "team212"
            + System.lineSeparator() + "2"
            + System.lineSeparator() + "34412iawnd019");

    MessageJson messageJson = new MessageJson(MessageType.SUPER_USER);
    when(connection.hasNext()).thenReturn(true);
    when(connection.next()).thenReturn(messageJson);

    mSuperUser.passControl(mScanner, connection);

    assertEquals("1. Get All Conversations for a particular User for user to user chat"
            + System.lineSeparator() +
            "2. Get All Conversations for a particular User for all group chats" + System.lineSeparator() +
            "3. Get All Conversations for a user for both user to user chat and group" +
            System.lineSeparator() + "4. Get All Conversations for a particular Group"
            + System.lineSeparator() + "Enter the group name for which you want to get " +
            "all the conversations from:" + System.lineSeparator() +
            "Do you want to get requested chats for specific dates or do you want to get all " +
            "dates of the requested types?" + System.lineSeparator() +
            "Press 1 to get all requested chats irrespective of dates" + System.lineSeparator() +
            "Press 2 to enter specific dates and get chats for those time frames" + System.lineSeparator() +
            "Dates entered should be of type: MM/DD/YYYY . You have to include \\/ in the date. " +
            "2 things to note: " + System.lineSeparator() +
            " 1) The start date is inclusive and end date is exclusive " + System.lineSeparator() +
            " 2) Adding invalid dates lead to removal of date criteria." + System.lineSeparator() +
            "Please enter the start date to get chats:" + System.lineSeparator() +
            "Invalid Date Entered. Date criteria removed" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "DONE" + System.lineSeparator() +
            "" + System.lineSeparator() +
            "Uh Oh! This is embarrassing. Something went wrong." + System.lineSeparator() +
            " Sending you back to main menu" + System.lineSeparator() +
            "1. Unread Messages" + System.lineSeparator() +
            "2. Chat user" + System.lineSeparator() +
            "3. Group Options" + System.lineSeparator() +
            "4. Follow User options" + System.lineSeparator() +
            "Or Enter \\q to quit" + System.lineSeparator() +
            "INPUT: Enter From above Options: " + System.lineSeparator(), mOutputStream.toString());
    mOutputStream.reset();
  }


}
