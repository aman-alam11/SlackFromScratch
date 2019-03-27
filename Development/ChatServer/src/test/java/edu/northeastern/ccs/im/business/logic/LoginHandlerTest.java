package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


import edu.northeastern.ccs.im.model.LoginCredentials;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.LoginHandler;


public class LoginHandlerTest {

  private LoginHandler loginHandler;
  private Gson mGson;

  @Mock
  private Connection connection;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    loginHandler = new LoginHandler();
    mGson = new Gson();
  }

  @Test
  public void handleMessageLoginPassTest() {

    LoginCredentials loginCredentials = new LoginCredentials("atti","pass");
    String message = mGson.toJson(loginCredentials);
    when(connection.signInUser("atti")).thenReturn(true);

    boolean b = loginHandler.handleMessage("user",message,connection);
     assertTrue(b);
  }

  @Test
  public void handleMessageLoginFailTest() {

    LoginCredentials loginCredentials = new LoginCredentials("atti","123");
    String message = mGson.toJson(loginCredentials);
    when(connection.signInUser("atti")).thenReturn(true);

    boolean b = loginHandler.handleMessage("user",message,connection);
    assertFalse(b);
  }

  @Test
  public void handleMessageFailTest() {

    boolean b = loginHandler.handleMessage("user",null,connection);
    assertFalse(b);
  }
}
