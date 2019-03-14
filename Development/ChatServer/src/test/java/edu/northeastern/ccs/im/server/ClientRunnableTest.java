package edu.northeastern.ccs.im.server;

import com.google.gson.Gson;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.concurrent.ScheduledFuture;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.business.logic.MessageHandler;
import edu.northeastern.ccs.im.business.logic.MessageHandlerFactory;
import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.LoginCredentials;

public class ClientRunnableTest {

	private Gson mGson;
	
	@Mock
	private NetworkConnection networkConnectionMock;
	@Mock
	private Connection connection;
	@Mock
	private Iterator<MessageJson> messageIterMock;
	@Mock
	private MessageJson messageMock;
	@Mock
	private ScheduledFuture<?> futureMock;
	@Mock
	private MessageHandler messageHandler;

	@Mock
	private MessageHandlerFactory messageHandlerFactory;
	
	private ClientRunnable clientRunnable;
	
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		when(networkConnectionMock.iterator()).thenReturn(messageIterMock);
		clientRunnable = new ClientRunnable(networkConnectionMock);
		clientRunnable.setFuture(futureMock);
		mGson = new Gson();

	}

	@Test
	public void testRun_whenMessageHasUserName() {

		LoginCredentials loginCredentials = new LoginCredentials("user","pass");
		String loginCredential = mGson.toJson(loginCredentials);
		MessageJson msgJson = new MessageJson("", MessageType.LOGIN, loginCredential);


		when(messageIterMock.hasNext()).thenReturn(true);
		when(messageIterMock.next()).thenReturn(msgJson);
		when(networkConnectionMock.getMessageHandlerFactory()).thenReturn(messageHandlerFactory);
		when(messageHandlerFactory.getMessageHandler(MessageType.LOGIN)).thenReturn(messageHandler);
		when(messageHandler.handleMessage(any(),any(), any())).thenReturn(true);

		when(messageMock.getMessageType()).thenReturn(MessageType.LOGIN);
		clientRunnable.run();
		assertEquals("user", clientRunnable.getUserName());
	}
	
	@Test
	public void testRun_whenMessageDoesNotHaveUserName() {
		when(networkConnectionMock.iterator()).thenReturn(messageIterMock);
		when(messageIterMock.hasNext()).thenReturn(true);
		when(messageIterMock.next()).thenReturn(messageMock);
		//when(messageMock.getName()).thenReturn(null);
		clientRunnable.run();
		assertEquals(null, clientRunnable.getUserName());
		// TODO: Check
//		assertEquals(-1, clientRunnable.getUserId());
		assertEquals(false, clientRunnable.isAuthenticated());
	}
	
	@Test
	public void testRun_whenMessageIteratorReturnsFalse() {
		when(networkConnectionMock.iterator()).thenReturn(messageIterMock);
		when(messageIterMock.hasNext()).thenReturn(false);
		clientRunnable.run();
		assertEquals(null, clientRunnable.getUserName());
		// TODO: Check
//		assertEquals(0, clientRunnable.getUserId());
	}
	
	@Test
	public void testRun_whenMessageIsTerminate() {
		initClient();
		assertEquals("testName", clientRunnable.getUserName());
		// TODO: Check
//		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		
		//when(messageMock.terminate()).thenReturn(true);
		clientRunnable.run();
		//clientRunnable.run();
		verify(networkConnectionMock, Mockito.times(1)).close();
		verify(futureMock, Mockito.atLeastOnce()).cancel(false);
		ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		// TODO: Check
//		verify(networkConnectionMock, Mockito.times(1)).sendMessage(messageArgumentCaptor.capture());
		Message msgSent = messageArgumentCaptor.getValue();
		assertEquals(null, msgSent.getText());
	}
	
	@Test
	public void testRun_whenMessageIsBroadcast() {
		initClient();
		assertEquals("testName", clientRunnable.getUserName());
		// TODO: Check
//		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		//when(messageMock.terminate()).thenReturn(false);
		//when(messageMock.isBroadcastMessage()).thenReturn(true);
		clientRunnable.run();
		clientRunnable.run();
	}
	
	
	@Test
	public void testRun_whenMessageIsNotBroadcast() {
		initClient();
		assertEquals("testName", clientRunnable.getUserName());
		// TODO: Check
//		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		//when(messageMock.terminate()).thenReturn(false);
		//when(messageMock.isBroadcastMessage()).thenReturn(false);
		clientRunnable.run();
		// TODO: Check
//		verify(networkConnectionMock, Mockito.times(0)).sendMessage(Mockito.any(Message.class));
	}
	
	@Test
	public void testRun_whenMessageIsIllegal_NameNotMatch() {
		initClient();
		assertEquals("testName", clientRunnable.getUserName());
		// TODO: Check

//		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		//when(messageMock.terminate()).thenReturn(false);
		//when(messageMock.isBroadcastMessage()).thenReturn(true);
		//when(messageMock.getName()).thenReturn("testName2");
		clientRunnable.run();
		ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		// TODO: Check
//		verify(networkConnectionMock, Mockito.times(1)).sendMessage(messageArgumentCaptor.capture());
		Message msgSent = messageArgumentCaptor.getValue();
		assertEquals("Last message was rejected because it specified an incorrect user name.",
				msgSent.getText());
	}
	
	@Test
	public void testRun_whenMessageIsIllegal_NameIsNull() {
		initClient();
		assertEquals("testName", clientRunnable.getUserName());
		// TODO: Check
//		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		//when(messageMock.terminate()).thenReturn(false);
		//when(messageMock.isBroadcastMessage()).thenReturn(true);
		//when(messageMock.getName()).thenReturn(null);
		clientRunnable.run();
		ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		// TODO: Check
//		verify(networkConnectionMock, Mockito.times(1)).sendMessage(messageArgumentCaptor.capture());
		Message msgSent = messageArgumentCaptor.getValue();
		assertEquals("Last message was rejected because it specified an incorrect user name.",
				msgSent.getText());
	}
	
	@Test
	public void testRun_whenIncomingMessageIsEmpty() {
		initClient();
		assertEquals("testName", clientRunnable.getUserName());
		// TODO: Check
//		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		when(messageIterMock.hasNext()).thenReturn(false);
		clientRunnable.run();
		// TODO: Check
//		verify(networkConnectionMock, Mockito.times(0)).sendMessage(Mockito.any(Message.class));
	}
	
	private void initClient() {
		when(networkConnectionMock.iterator()).thenReturn(messageIterMock);
		when(messageIterMock.hasNext()).thenReturn(true);
		when(messageIterMock.next()).thenReturn(messageMock);
		//when(messageMock.getName()).thenReturn("testName");
		clientRunnable.run();
	}
	
	
}
