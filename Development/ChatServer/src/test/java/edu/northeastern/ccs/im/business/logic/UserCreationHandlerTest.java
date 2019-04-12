package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.LoginCredentials;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.UserCreationHandler;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserCreationHandlerTest {

  private UserCreationHandler userCreationHandler;
  private Gson mGson;

  @Mock
  private Connection connection;
  @Mock
  private JPAService jpaServiceMock;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    userCreationHandler = new UserCreationHandler();
    mGson = new Gson();
    JPAService.setJPAService(jpaServiceMock);
    when(jpaServiceMock.createUser(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
  }

  @Test
  public void handleMessageCreateUserTest() {

  	//when(jpaServiceMock.createUser(Mockito.eq("atti"))).thenReturn(null);
    LoginCredentials loginCredentials = new LoginCredentials("atti","pass");
    String message = mGson.toJson(loginCredentials);
    assertFalse(userCreationHandler.handleMessage("user",message,connection));
  }
  
  @Test
  public void handleMessageCreateUserTest_Pass() {

  	//when(jpaServiceMock.findUserByName(Mockito.eq("atti1"))).thenReturn(null);
  	
  	when(connection.signInUser(Mockito.anyString())).thenReturn(true);
    LoginCredentials loginCredentials = new LoginCredentials("atti1","pass");
    String message = mGson.toJson(loginCredentials);
    assertTrue(userCreationHandler.handleMessage("user",message,connection));
  }
}
