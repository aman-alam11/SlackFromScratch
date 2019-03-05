package edu.northeastern.ccs.im.clientmenu;

import org.junit.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.clientmenu.clientinterfaces.ParentModel;
import edu.northeastern.ccs.im.clientmenu.login.Registration;

public class RegistrationTest {

  @Mock
  ParentModel parentModel;

  @Test
  public void successfulAcountCreationTest() {
    Registration login = new Registration();

    String str = "atti\n" +  "fdf\n" + "fdf\n" + "\\q";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    login.passControl(scanner, parentModel);
  }

  @Test
  public void unSuccessfulAcountCreationTest() {
    Registration login = new Registration();

    String str = "atti\n" +  "fdf\n" + "ff\n" + "\\q";

    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    login.passControl(scanner, parentModel);
  }
}
