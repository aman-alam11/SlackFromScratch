package edu.northeastern.ccs.im.business.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.model.UserFollwingList;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.ListFollowerUserHandler;

public class ListFollowerUserHandlerTest {
	@Mock
	private JPAService jpaService;
	@Mock
	private Connection connection;
	private ArgumentCaptor<MessageJson> argument = ArgumentCaptor.forClass(MessageJson.class);
	private Gson gson = new Gson();
	private ListFollowerUserHandler listFollowerUserHandler;
	
	@Before
	public void init()  {
		MockitoAnnotations.initMocks(this);
		JPAService.setJPAService(jpaService);
		
		listFollowerUserHandler = new ListFollowerUserHandler();
	}
	
	@Test
	public void test_handleMesage() {
		List<User> userList = new  ArrayList<>();
		User u1 = new User("user1","","");
		User u2 = new User("user2","","");
		userList.add(u1);
		userList.add(u2);
		ListFollowerUserHandler spyList = Mockito.spy(listFollowerUserHandler);
		when(jpaService.getAllFollowers(Mockito.eq("name1"))).thenReturn(userList);
		Mockito.doNothing().when(spyList).sendResponse(Mockito.any(), Mockito.any(Connection.class));
		boolean result = spyList.handleMessage("name1", "", connection);
		Mockito.verify(spyList, Mockito.times(1)).sendResponse(argument.capture(), Mockito.any(Connection.class));
		UserFollwingList userFollowingList = gson.fromJson(argument.getValue().getMessage(), UserFollwingList.class);
		assertEquals(2, userFollowingList.getUserList().size());
		assertEquals("user1", userFollowingList.getUserList().get(0).getUserName());
		assertEquals("user2", userFollowingList.getUserList().get(1).getUserName());
		assertTrue(result);
	}
	
	@Test
	public void test_handleMesage_error() {
		List<User> userList = new  ArrayList<>();
		User u1 = new User("user1","","");
		User u2 = new User("user2","","");
		userList.add(u1);
		userList.add(u2);
		when(jpaService.getAllFollowers(Mockito.eq("name1"))).thenThrow(new RuntimeException());
		boolean result = listFollowerUserHandler.handleMessage("name1", "", connection);
		assertFalse(result);
	}
	
}
