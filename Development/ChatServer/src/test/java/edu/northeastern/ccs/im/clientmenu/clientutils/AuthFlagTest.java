package edu.northeastern.ccs.im.clientmenu.clientutils;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.AckModel;

public class AuthFlagTest {


  @Mock
  Connection connection;

  private AuthFlag authFlag;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    authFlag = new AuthFlag(true);
  }

  @Test
  public void isUserAuthTest() {
    assertTrue(AuthFlag.isUserAuthenticated());
  }

  @Test
  public void resetFlagTest() {
    authFlag.resetFlags();
    assertFalse(AuthFlag.isUserAuthenticated());
  }

  @Test
  public void reAuthenticateWithHandshakeTest() {
    MessageJson messageJson = new MessageJson("from",MessageType.LOGIN,"message");
    authFlag.reAuthenticateWithHandshake(messageJson, connection);
  }

  @Test
  public void reAuthenticateWithHandshakeFalseMessageTest() {
    MessageJson messageJson = new MessageJson("from",MessageType.USER_CHAT,"message");
    authFlag.reAuthenticateWithHandshake(messageJson, connection);
  }


  @Test
  public void listenTest() {
    AckModel ackModel = new AckModel(true,"hello", false);
    String str = new Gson().toJson(ackModel);
    authFlag.listen(str);
  }


}
