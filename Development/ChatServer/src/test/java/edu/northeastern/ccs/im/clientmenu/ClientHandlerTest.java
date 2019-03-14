package edu.northeastern.ccs.im.clientmenu;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.view.FrontEnd;

public class ClientHandlerTest {

  @Mock
  private Connection connection;

  private ClientHandler clientHandler;



  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    clientHandler = new ClientHandler(connection);
  }


  @Test
  public void loginTest() {

    String str = "1\n" + "atti\n" + "pass\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    clientHandler.initClientOperations(scanner);
  }

  @Test
  public void loginQuitTest() {

    String str = "\\q\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    clientHandler.initClientOperations(scanner);
  }

  @Test
  public void loginWrongInputStringTest() {
    String str = "ads\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    clientHandler.initClientOperations(scanner);
  }

  @Test
  public void loginWrongInputIntTest() {
    String str = "67\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    clientHandler.initClientOperations(scanner);
  }

  @Test
  public void constructorNullTest() {
    try {
      ClientHandler clientHandler1 = new ClientHandler(null);
    }
    catch (IllegalArgumentException e) {
      FrontEnd.getView().sendToView(e.toString());
    }

  }

}
