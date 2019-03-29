package edu.northeastern.ccs.im.clientmenu.grouplevel;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageConstants;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;
import edu.northeastern.ccs.im.model.ErrorCodes;

public class AddUserToGroupModelLayerTest {
  private final static String USERNAME = "atti\n";
  private final static String QUIT = "\\q\n";
  private Gson gson;

  @Mock
  Connection connection;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    gson = new Gson();
  }

  @Test
  public void passControlTest() {
    String str =  USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    AckModel ackModel = new AckModel();
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, gson.toJson(ackModel));
    when(connection.next()).thenReturn(response);

    AddUserToGroupModelLayer addUserToGroupModelLayer = new AddUserToGroupModelLayer();
    addUserToGroupModelLayer.passControl(scanner,connection);
  }

  @Test
  public void errorCodeNotEmptyTest() {
    String str =  USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    AckModel ackModel = new AckModel();
    ackModel.addErrorCode(ErrorCodes.G801);
    ackModel.addErrorCode(ErrorCodes.G802);
    ackModel.appendErrorMessage("Error");
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, gson.toJson(ackModel));
    when(connection.next()).thenReturn(response);

    AddUserToGroupModelLayer addUserToGroupModelLayer = new AddUserToGroupModelLayer();
    addUserToGroupModelLayer.passControl(scanner,connection);
  }

  @Test
  public void errorCodeAppendMessageEmptyTest() {
    String str =  USERNAME;
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes());
    Scanner scanner = new Scanner(in);

    when(connection.hasNext()).thenReturn(true);
    AckModel ackModel = new AckModel();
    ackModel.addErrorCode(ErrorCodes.G801);
    ackModel.addErrorCode(ErrorCodes.G802);
    MessageJson response = new MessageJson(MessageConstants.SYSTEM_MESSAGE, MessageType.AUTH_ACK, gson.toJson(ackModel));
    when(connection.next()).thenReturn(response);

    AddUserToGroupModelLayer addUserToGroupModelLayer = new AddUserToGroupModelLayer();
    addUserToGroupModelLayer.passControl(scanner,connection);
  }
}
