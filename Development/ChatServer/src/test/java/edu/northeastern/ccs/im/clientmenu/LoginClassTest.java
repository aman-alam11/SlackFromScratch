package edu.northeastern.ccs.im.clientmenu;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;


import com.google.gson.Gson;


import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.loginlevel.Login;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;

public class LoginClassTest {
  private Gson mGson;

  private static final String USERNAME = "atti\n";
  private static final String PASSWORD = "atti\n";

  @Mock
  Connection parentModel;
  @Mock
  Login login;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    login = new Login();
    mGson = new Gson();
  }


  @Test
  public void loginSuccessFullTest() {

    String str = USERNAME +  PASSWORD;

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    when(parentModel.hasNext()).thenReturn(true);
    AckModel responseMessage = new AckModel(true, MessageConstants.LOGIN_SUCCESS, true);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            mGson.toJson(responseMessage));
    when(parentModel.next()).thenReturn(responsePacket);
    login.passControl(scanner, parentModel);
  }

  @Test
  public void loginFailed() {
    String str = USERNAME + PASSWORD;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(parentModel.hasNext()).thenReturn(true);
    AckModel responseMessage = new AckModel(false, MessageConstants.LOGIN_SUCCESS, true);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            mGson.toJson(responseMessage));
    when(parentModel.next()).thenReturn(responsePacket);

    login.passControl(scanner, parentModel);

  }

  @Test
  public void listnerNotLogin() {
    String str = USERNAME +  PASSWORD;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(parentModel.hasNext()).thenReturn(true);
    AckModel responseMessage = new AckModel(true, MessageConstants.LOGIN_SUCCESS, false);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            mGson.toJson(responseMessage));
    when(parentModel.next()).thenReturn(responsePacket);

    login.passControl(scanner, parentModel);

  }

  @Test
  public void modelNullTest() {
    String str = USERNAME +  PASSWORD;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(parentModel.hasNext()).thenReturn(true);
    AckModel responseMessage = new AckModel(true, MessageConstants.LOGIN_SUCCESS, false);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            mGson.toJson(responseMessage));
    when(parentModel.next()).thenReturn(responsePacket);

    login.passControl(scanner, null);

  }

  @Test
  public void blankResponseLoginTest() {
    String str = USERNAME +  PASSWORD;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(parentModel.hasNext()).thenReturn(true);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            "");

    when(parentModel.next()).thenReturn(responsePacket);

    login.passControl(scanner, parentModel);

  }
}
