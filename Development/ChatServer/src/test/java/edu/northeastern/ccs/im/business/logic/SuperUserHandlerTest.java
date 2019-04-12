package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import edu.northeastern.ccs.im.model.SuperUserMessageModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.SuperUserHandler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SuperUserHandlerTest {

  private ByteArrayOutputStream mOutputStream;
  private SuperUserHandler mSuperUserHandler;

  @Mock
  Connection connection;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);

    mSuperUserHandler = new SuperUserHandler();
    mOutputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(mOutputStream));
  }

  @After
  public void deInit() {
    System.setOut(System.out);
    System.setIn(System.in);
  }

  @Test
  public void testNotASuperUser() {
    // Not a SuperUser as message is null
    assertFalse(mSuperUserHandler.handleMessage(null, "", null));

    assertFalse(mSuperUserHandler.handleMessage(null, null, null));
  }

  @Test
  public void testNullUserAndValidUser() {
    SuperUserMessageModel messageModel = new SuperUserMessageModel(true,
            false, "nsa");
    assertTrue(mSuperUserHandler.handleMessage("nsa", new Gson().toJson(messageModel), connection));
  }

  @Test
  public void testGetUserChat() {
    SuperUserMessageModel messageModel = new SuperUserMessageModel(true,
            false, "nsa");
    assertTrue(mSuperUserHandler.handleMessage("nsa", new Gson().toJson(messageModel), connection));
  }

  @Test
  public void testGetGroupChats() {
    SuperUserMessageModel messageModel = new SuperUserMessageModel(false,
            true, "nsa");
    assertTrue(mSuperUserHandler.handleMessage("nsa", new Gson().toJson(messageModel), connection));
  }

  @Test
  public void testGetUserGroupChats() {
    SuperUserMessageModel messageModel = new SuperUserMessageModel(true,
            true, "nsa");
    assertTrue(mSuperUserHandler.handleMessage("nsa", new Gson().toJson(messageModel), connection));
  }

  @Test
  public void testGetChatsForGroup() {
    SuperUserMessageModel messageModel = new SuperUserMessageModel("team212akwjnd");
    assertFalse(mSuperUserHandler.handleMessage("nsa", new Gson().toJson(messageModel), connection));
  }
}
