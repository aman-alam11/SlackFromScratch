package edu.northeastern.ccs.im.business.logic;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.database.Group;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;
import edu.northeastern.ccs.im.model.AddDeleteGroupUsers;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.AddGroupUsersHandler;

public class AddGroupUsersHandlerTest {
	
	@Mock
	private JPAService jpaService;
	@Mock
	private Connection connection;
	private Gson gson = new Gson();
	private AddGroupUsersHandler addGroupUserHandler;
	
	@Before
	public void init()  {
		MockitoAnnotations.initMocks(this);
		JPAService.setJPAService(jpaService);
	}
	
	@Test
	public void test_InvalidGroup() {
		when(jpaService.findGroupByName(Mockito.eq("grp"))).thenReturn(null);
		AddDeleteGroupUsers message = new AddDeleteGroupUsers();
		message.setGroupName("grp");
		message.setUsersList(Arrays.asList("name1"));
		addGroupUserHandler = new AddGroupUsersHandler();
		boolean b = addGroupUserHandler.handleMessage("sender", gson.toJson(message), connection);
		assertFalse(b);
	}
	
	@Test
	public void test_InvalidRequestor() {
		when(jpaService.findGroupByName(Mockito.eq("grp"))).thenReturn(new Group());
		when(jpaService.findNonMembers(Mockito.anyList(),Mockito.eq("grp"))).thenReturn(new ArrayList<>());
		when(jpaService.findUserByName(Mockito.anyString())).thenReturn(null);
		AddDeleteGroupUsers message = new AddDeleteGroupUsers();
		message.setGroupName("grp");
		message.setUsersList(Arrays.asList("name1"));
		addGroupUserHandler = new AddGroupUsersHandler();
		boolean b = addGroupUserHandler.handleMessage("sender", gson.toJson(message), connection);
		assertFalse(b);
	}
	
	@Test
	public void test_InvalidRequestor2() {
		when(jpaService.findGroupByName(Mockito.eq("grp"))).thenReturn(new Group());
		when(jpaService.findNonMembers(Mockito.anyList(),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		when(jpaService.findUserByName(Mockito.anyString())).thenReturn(null);
		AddDeleteGroupUsers message = new AddDeleteGroupUsers();
		message.setGroupName("grp");
		message.setUsersList(Arrays.asList("name1"));
		addGroupUserHandler = new AddGroupUsersHandler();
		boolean b = addGroupUserHandler.handleMessage("sender", gson.toJson(message), connection);
		assertFalse(b);
	}
	
	@Test
	public void test_validUserButAlreadyInGroup() {
		when(jpaService.findGroupByName(Mockito.eq("grp"))).thenReturn(new Group());
		when(jpaService.findNonMembers(Mockito.anyList(),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		when(jpaService.findUserByName(Mockito.anyString())).thenReturn(new User());
		AddDeleteGroupUsers message = new AddDeleteGroupUsers();
		message.setGroupName("grp");
		message.setUsersList(Arrays.asList("name1"));
		addGroupUserHandler = new AddGroupUsersHandler();
		boolean b = addGroupUserHandler.handleMessage("sender", gson.toJson(message), connection);
		assertFalse(b);
	}

	@Test
	public void test_validUserToAddToGroup() {
		when(jpaService.findGroupByName(Mockito.eq("grp"))).thenReturn(new Group());
		when(jpaService.findNonMembers(Mockito.anyList(),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		//when(jpaService.findNonMembers(Mockito.eq(Arrays.asList("name1")),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		when(jpaService.findUserByName(Mockito.any())).thenReturn(new User());
		AddDeleteGroupUsers message = new AddDeleteGroupUsers();
		message.setGroupName("grp");
		message.setUsersList(Arrays.asList("name1"));
		addGroupUserHandler = new AddGroupUsersHandler();
		boolean b = addGroupUserHandler.handleMessage("sender", gson.toJson(message), connection);
		assertFalse(b);
	}
	
	@Test
	public void test_userToAddIsEmpty() {
		when(jpaService.findGroupByName(Mockito.eq("grp"))).thenReturn(new Group());
		when(jpaService.findNonMembers(Mockito.anyList(),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		//when(jpaService.findNonMembers(Mockito.eq(Arrays.asList("name1")),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		when(jpaService.findUserByName(Mockito.any())).thenReturn(new User());
		AddDeleteGroupUsers message = new AddDeleteGroupUsers();
		message.setGroupName("grp");
		message.setUsersList(new ArrayList<String>());
		addGroupUserHandler = new AddGroupUsersHandler();
		boolean b = addGroupUserHandler.handleMessage("sender", gson.toJson(message), connection);
		assertFalse(b);
	}
	
	@Test
	public void test_invalidUserList() {
		when(jpaService.findGroupByName(Mockito.eq("grp"))).thenReturn(new Group());
		when(jpaService.findNonMembers(Mockito.anyList(),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		//when(jpaService.findNonMembers(Mockito.eq(Arrays.asList("name1")),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		when(jpaService.findUserByName("grp")).thenReturn(new User());
		when(jpaService.findUserByName("a")).thenReturn(new User());
		when(jpaService.findUserByName("b")).thenReturn(null);
		AddDeleteGroupUsers message = new AddDeleteGroupUsers();
		message.setGroupName("grp");
		message.setUsersList(Arrays.asList("a","b"));
		addGroupUserHandler = new AddGroupUsersHandler();
		boolean b = addGroupUserHandler.handleMessage("sender", gson.toJson(message), connection);
		assertFalse(b);
	}
	
	@Test
	public void test_5() {
		when(jpaService.findGroupByName(Mockito.eq("grp"))).thenReturn(new Group());
		when(jpaService.findNonMembers(Mockito.anyList(),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		//when(jpaService.findNonMembers(Mockito.eq(Arrays.asList("name1")),Mockito.eq("grp"))).thenReturn(Arrays.asList(new User()));
		when(jpaService.findUserByName("grp")).thenReturn(new User());
		when(jpaService.findUserByName("a")).thenReturn(new User());
		when(jpaService.findUserByName("c")).thenReturn(new User());
		when(jpaService.findUserByName("b")).thenReturn(null);
		AddDeleteGroupUsers message = new AddDeleteGroupUsers();
		message.setGroupName("grp");
		message.setUsersList(Arrays.asList("a","b", "c"));
		addGroupUserHandler = new AddGroupUsersHandler();
		boolean b = addGroupUserHandler.handleMessage("sender", gson.toJson(message), connection);
		assertFalse(b);
	}
	
}
