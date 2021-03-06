package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import edu.northeastern.ccs.im.model.LoginCredentials;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.UserSearchHandler;


public class SearchHandlerTest {

  private UserSearchHandler userSearchHandler;
  private Gson mGson;

  @Mock
  private Connection connection;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    userSearchHandler = new UserSearchHandler();
    mGson = new Gson();
  }

  @Test
  public void handleMessageUserSearchTrueTest() {

    LoginCredentials loginCredentials = new LoginCredentials("atti","atti");
    String message = mGson.toJson(loginCredentials);
    boolean b = userSearchHandler.handleMessage("user",message,connection);
    //assertTrue(b);
  }

  @Test
  public void handleMessageUserSearchFalseTest() {

    LoginCredentials loginCredentials = new LoginCredentials("atti","pass");
    String message = mGson.toJson(loginCredentials);
    boolean b = userSearchHandler.handleMessage("user",message,null);
    assertFalse(b);
  }
}
