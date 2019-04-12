package edu.northeastern.ccs.im.business.logic;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.UserFollow;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.AddFollowerUserHandler;

public class AddFollowerUserHandlerTest {
	@Mock
	private JPAService jpaService;
	@Mock
	private Connection connection;
	private Gson gson = new Gson();
	private AddFollowerUserHandler addFollowerUserHandler;
	
	@Before
	public void init()  {
		MockitoAnnotations.initMocks(this);
		JPAService.setJPAService(jpaService);
		addFollowerUserHandler = new AddFollowerUserHandler();
	}
	
	@Test
	public void test_handleMesage() {
		when(jpaService.addFollower(Mockito.eq("name1"), Mockito.eq("name2"))).thenReturn(true);
		UserFollow uf = new UserFollow("name1", "name2");
		boolean result = addFollowerUserHandler.handleMessage("name1", gson.toJson(uf), connection);
		assertTrue(result);
	}
	
	@Test
	public void test_handleMesage_exception() {
		when(jpaService.addFollower(Mockito.eq("name1"), Mockito.eq("name2"))).thenThrow(new RuntimeException());
		UserFollow uf = new UserFollow("name1", "name2");
		boolean result = addFollowerUserHandler.handleMessage("name1", gson.toJson(uf), connection);
		assertFalse(result);
	}

}
