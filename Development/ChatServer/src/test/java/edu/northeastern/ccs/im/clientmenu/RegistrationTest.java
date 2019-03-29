package edu.northeastern.ccs.im.clientmenu;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.loginlevel.Registration;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;

import static org.mockito.Mockito.when;

public class RegistrationTest {

  private Gson mGson;

  @Mock
  Registration  registration;

  @Mock
  Connection parentModel;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    registration = new Registration();
    mGson = new Gson();
  }

  @Test
  public void successfulAcountCreationTest() {

    String str = "atti\n" +  "fdf\n" + "fdf\n";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(parentModel.hasNext()).thenReturn(true);
    AckModel responseMessage = new AckModel(true, MessageConstants.REGISTRATION_SUCCESS, false);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            mGson.toJson(responseMessage));
    when(parentModel.next()).thenReturn(responsePacket);

    registration.passControl(scanner, parentModel);
  }

  @Test
  public void passwordsDonotMatchTest() {

    String str = "atti\n" +  "df\n" + "ff\n" + "\\q";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    registration.passControl(scanner, parentModel);
  }

  @Test
  public void modelNullRegistrationTest() {

    String str = "atti\n" +  "df\n" + "ff\n" + "\\q";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    registration.passControl(scanner, null);
  }

  @Test
  public void isLoginTest() {

    String str = "atti\n" +  "df\n" + "df\n" + "\\q";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    when(parentModel.hasNext()).thenReturn(true);
    AckModel responseMessage = new AckModel(false, MessageConstants.REGISTRATION_FAILURE, true);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            mGson.toJson(responseMessage));
    when(parentModel.next()).thenReturn(responsePacket);
    registration.passControl(scanner, parentModel);
  }

  @Test
  public void registrationFailedTest() {

    String str = "atti\n" +  "ff\n" + "ff\n" + "\\q";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(parentModel.hasNext()).thenReturn(true);
    AckModel responseMessage = new AckModel(false, MessageConstants.REGISTRATION_FAILURE, false);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            mGson.toJson(responseMessage));
    when(parentModel.next()).thenReturn(responsePacket);

    registration.passControl(scanner, parentModel);
  }

  @Test
  public void blankResponseRegistrationTest() {
    String str = "atti\n" +  "ff\n" + "ff\n" + "\\q";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(parentModel.hasNext()).thenReturn(true);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            "");

    when(parentModel.next()).thenReturn(responsePacket);

    registration.passControl(scanner, parentModel);

  }

}
