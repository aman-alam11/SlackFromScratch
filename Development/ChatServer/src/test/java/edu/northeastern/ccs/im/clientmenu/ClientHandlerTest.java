package edu.northeastern.ccs.im.clientmenu;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.clientmenu.clientutils.CurrentLevel;
import edu.northeastern.ccs.im.clientmenu.clientutils.InjectLevelUtil;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.view.FrontEnd;

import static org.mockito.Mockito.when;

public class ClientHandlerTest {

  @Mock
  private Connection connection;

  private ClientHandler clientHandler;
  private Gson mGson;



  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    clientHandler = new ClientHandler(connection);
    mGson = new Gson();
  }


  @Test
  public void loginTest() {

    String str = "1\n" + "atti\n" + "pass\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    when(connection.hasNext()).thenReturn(true);
    AckModel responseMessage = new AckModel(true, MessageConstants.LOGIN_SUCCESS, true);
    MessageJson responsePacket = new MessageJson(MessageConstants.SYSTEM_MESSAGE,
            MessageType.AUTH_ACK,
            mGson.toJson(responseMessage));
    when(connection.next()).thenReturn(responsePacket);
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
    ClientHandler clientHandler1 = new ClientHandler(null);
    clientHandler1.toString();

  }

  @Test
  public void backTest() {
    String str = "\\b\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    clientHandler.initClientOperations(scanner);
  }

  @Test
  public void backFromAnotherLevelTest() {
    String str = "\\b\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.GROUP_LEVEL);
    clientHandler.initClientOperations(scanner);
  }

  @Test
  public void backFromSameLevelTest() {
    String str = "\\b\n";
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);
    InjectLevelUtil.getInstance().injectLevel(CurrentLevel.USER_LEVEL);
    clientHandler.initClientOperations(scanner);
  }

}
