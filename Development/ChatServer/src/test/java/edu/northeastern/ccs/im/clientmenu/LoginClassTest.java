package edu.northeastern.ccs.im.clientmenu;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.google.gson.Gson;


import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.loginlevel.Login;
import edu.northeastern.ccs.im.model.AckModel;

public class LoginClassTest {
  private Gson mGson;

  private static final String USERNAME = "atti\n";
  private static final String PASSWORD = "pass\n";

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
  public void loginTest() {

    String str = USERNAME +  PASSWORD;

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    login.passControl(scanner, parentModel);
  }

  @Test
  public void listnerLoginFailed() {
    AckModel ackModel = new AckModel(false, "error msg", true);
    String jsonAck = mGson.toJson(ackModel);
    String str = USERNAME + PASSWORD;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    login.passControl(scanner, parentModel);
    login.listen(jsonAck);
  }

  @Test
  public void listnerLoginSuccessful() {
    AckModel ackModel = new AckModel(true, "error msg", true);
    String jsonAck = mGson.toJson(ackModel);
    String str = USERNAME +  PASSWORD;

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    login.passControl(scanner, parentModel);
    login.listen(jsonAck);
  }

  @Test
  public void listnerNotLogin() {
    AckModel ackModel = new AckModel(false, "error msg", false);
    String jsonAck = mGson.toJson(ackModel);
    String str = USERNAME +  PASSWORD;

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    login.passControl(scanner, parentModel);
    login.listen(jsonAck);
  }
}
