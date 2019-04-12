package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertFalse;

import edu.northeastern.ccs.im.model.LoginCredentials;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.UserCreationHandler;


public class UserCreationHandlerTest {

  private UserCreationHandler userCreationHandler;
  private Gson mGson;

  @Mock
  private Connection connection;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    userCreationHandler = new UserCreationHandler();
    mGson = new Gson();
  }

  //@Test
  public void handleMessageCreateUserTest() {

    LoginCredentials loginCredentials = new LoginCredentials("atti","pass");
    String message = mGson.toJson(loginCredentials);
    userCreationHandler.handleMessage("user",message,connection);
  }
}
