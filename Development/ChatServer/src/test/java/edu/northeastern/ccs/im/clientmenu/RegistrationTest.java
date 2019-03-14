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
import edu.northeastern.ccs.im.model.AckModel;

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

    registration.passControl(scanner, parentModel);
  }

  @Test
  public void unSuccessfulAcountCreationTest() {

    String str = "atti\n" +  "fdf\n" + "ff\n" + "\\q";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    registration.passControl(scanner, parentModel);
  }

  @Test
  public void listnerIsLoginTest() {
    AckModel ackModel = new AckModel(true, "error msg", true);
    String jsonAck = mGson.toJson(ackModel);
    registration.listen(jsonAck);
  }

  @Test
  public void listnerIsUserAuthTest() {
    AckModel ackModel = new AckModel(true, "error msg", false);
    String jsonAck = mGson.toJson(ackModel);
    registration.listen(jsonAck);
  }

  @Test
  public void listnerIsUserNotAuthTest() {
    AckModel ackModel = new AckModel(false, "error msg", false);
    String jsonAck = mGson.toJson(ackModel);
    registration.listen(jsonAck);
  }
}
