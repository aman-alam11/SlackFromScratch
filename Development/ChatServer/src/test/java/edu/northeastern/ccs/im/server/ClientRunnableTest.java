package edu.northeastern.ccs.im.server;

import com.google.gson.Gson;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ScheduledFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import edu.northeastern.ccs.im.ClientState;
import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.business.logic.MessageHandler;
import edu.northeastern.ccs.im.business.logic.MessageHandlerFactory;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.model.LoginCredentials;

public class ClientRunnableTest {

	private Gson mGson;
	
	@Mock
	private NetworkConnection networkConnectionMock;

	@Mock
	private Iterator<MessageJson> messageIterMock;

	@Mock
	private ScheduledFuture<?> futureMock;

	@Mock
	private MessageHandler messageHandler;

	@Mock
	private MessageHandlerFactory messageHandlerFactory;

	private ClientRunnable clientRunnable;

	@Mock
	private ClientTimer clientTimer;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		when(networkConnectionMock.iterator()).thenReturn(messageIterMock);
		clientRunnable = new ClientRunnable(networkConnectionMock);
		clientRunnable.setFuture(futureMock);
		mGson = new Gson();

	}

	@Test
	public void testRun_whenMessageUnauthenticatedUser() {

		LoginCredentials loginCredentials = new LoginCredentials("user","pass");
		String loginCredential = mGson.toJson(loginCredentials);
		MessageJson msgJson = new MessageJson("", MessageType.LOGIN, loginCredential);


		when(messageIterMock.hasNext()).thenReturn(true);
		when(messageIterMock.next()).thenReturn(msgJson);
		when(networkConnectionMock.getMessageHandlerFactory()).thenReturn(messageHandlerFactory);
		when(messageHandlerFactory.getMessageHandler(MessageType.LOGIN)).thenReturn(messageHandler);
		when(messageHandler.handleMessage(any(),any(), any())).thenReturn(true);
		clientRunnable.run();
		assertNull(null, clientRunnable.getUserName());
	}


	@Test
	public void testRun_whenUserSendingChat() {

		ChatModel chatModel = new ChatModel("atti","singh","hello", new Date(), true);
		String loginCredential = mGson.toJson(chatModel);
		MessageJson msgJson = new MessageJson("", MessageType.USER_CHAT, loginCredential);

		try {
			Field state = clientRunnable.getClass().getDeclaredField("state");
			state.setAccessible(true);
			state.set(clientRunnable,ClientState.LOGGED_IN);
			when(messageIterMock.hasNext()).thenReturn(true);
			when(messageIterMock.next()).thenReturn(msgJson);


			when(networkConnectionMock.getMessageHandlerFactory()).thenReturn(messageHandlerFactory);
			when(messageHandlerFactory.getMessageHandler(Matchers.eq(MessageType.USER_CHAT))).thenReturn(messageHandler);
			when(messageHandler.handleMessage(Matchers.contains("User"),any(), any())).thenReturn(true);
			clientRunnable.run();
			assertNull(clientRunnable.getUserName());


		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testRun_timerIsBehind() {
		Field timer = null;
		try {
			timer = clientRunnable.getClass().getDeclaredField("timer");
			timer.setAccessible(true);
			when(clientTimer.isBehind()).thenReturn(true);
			timer.set(clientRunnable, clientTimer);
			clientRunnable.run();
			assertNull(clientRunnable.getUserName());
		} catch (NoSuchFieldException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}


	@Test
	public void testRun_outGoingMessage() {

		ChatModel chatModel = new ChatModel("atti","singh","hello", new Date(), true);
		String loginCredential = mGson.toJson(chatModel);
		MessageJson msgJson = new MessageJson("", MessageType.USER_CHAT, loginCredential);
		MessageJson msgJson1 = new MessageJson("", MessageType.USER_CHAT, loginCredential);
		clientRunnable.enqueueMessage(msgJson);
		clientRunnable.enqueueMessage(msgJson1);
		clientRunnable.run();
		assertNull(clientRunnable.getUserName());
	}


	@Test
	public void testRun_methodTests() {
		clientRunnable.setState(ClientState.LOGGED_IN);
		assertEquals(ClientState.LOGGED_IN, clientRunnable.getState());
	}

	@Test
	public void testRun_SignInUser() {
		clientRunnable.signInUser("user");
		assertEquals("user", clientRunnable.getUserName());
	}

	@Test
	public void testRun_SignInUserNullTest() {
		clientRunnable.signInUser(null);
		assertNull(clientRunnable.getUserName());
	}

	@Test
	public void testRun_messageIncomingFalseTest() {
		clientRunnable.setState(ClientState.LOGGED_IN);
		when(messageIterMock.hasNext()).thenReturn(false);
		clientRunnable.run();
		assertNull(clientRunnable.getUserName());
	}

}
