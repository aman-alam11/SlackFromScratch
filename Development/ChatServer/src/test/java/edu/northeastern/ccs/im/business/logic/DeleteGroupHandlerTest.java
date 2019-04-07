package edu.northeastern.ccs.im.business.logic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.DeleteGroupHandler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DeleteGroupHandlerTest {

  private DeleteGroupHandler deleteGroupHandler;


  @Mock
  private Connection connection;

  @Mock
  private JPAService jpaService;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    JPAService.setJPAService(jpaService);
    deleteGroupHandler = new DeleteGroupHandler();
  }

  @Test
  public void handleMessageTrueTest() {

    when(JPAService.getInstance().deleteGroup(any())).thenReturn(true);

    boolean b = deleteGroupHandler.handleMessage("user","",connection);
    assertTrue(b);

  }

  @Test
  public void handleMessageExceptionCatchTest() {

    when(JPAService.getInstance().deleteGroup(any())).thenReturn(true);

    boolean b = deleteGroupHandler.handleMessage("user","",null);
    assertFalse(b);

  }

}
