package edu.northeastern.ccs.im.clientmenu.userlevel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;

public class UnreadMessagesTest {


  private UnreadMessages unreadMessages;

  @Mock
  Connection connection;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    unreadMessages = new UnreadMessages();
  }

  @Test(expected = UnsupportedOperationException.class)
  public void passControlUnreadMessagesTest() {

    String str = "";
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(byteArrayInputStream);
    unreadMessages.passControl(scanner, connection);
  }
}
