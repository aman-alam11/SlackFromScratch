package edu.northeastern.ccs.im.clientmenu;

import org.junit.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.ParentModel;
import edu.northeastern.ccs.im.clientmenu.login.Login;
import edu.northeastern.ccs.im.view.FrontEnd;

import static org.junit.Assert.assertEquals;

public class LoginClassTest {

  @Mock
  ParentModel parentModel;



  @Test
  public void logoutTest() {
    Login login = new Login();

    String str = "atti\n" +  "fdf\n" + "logout\n" + "\\q";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    login.passControl(scanner, parentModel);
  }

  @Test
  public void wrongInputTest() {
    Login login = new Login();

    String str = "atti\n" +  "fdf\n" + "log\n";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    login.passControl(scanner, parentModel);
  }

}
