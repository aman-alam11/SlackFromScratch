package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertTrue;

import edu.northeastern.ccs.im.model.GroupSearchModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.GroupSearchHandler;


public class GroupSearchHandlerTest {

  @Mock
  private Connection connection;

  private GroupSearchHandler groupSearchHandler;
  private Gson gson;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    groupSearchHandler = new GroupSearchHandler();
    gson = new Gson();
  }

  @Test
  public void handleMessageTest() {
    GroupSearchModel groupSearch = new GroupSearchModel("nnn");
    boolean b = groupSearchHandler.handleMessage("user", gson.toJson(groupSearch), connection);
    assertTrue(b);
  }

}
