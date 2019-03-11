package edu.northeastern.ccs.im.server;

import static org.junit.Assert.assertEquals;
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
import edu.northeastern.ccs.im.message.MessageJson;

public class ClientRunnableTest {
	
	@Mock
	private NetworkConnection networkConnectionMock;
	@Mock
	private Iterator<MessageJson> messageIterMock;
	@Mock
	private MessageJson messageMock;
	@Mock
	private ScheduledFuture<?> futureMock;
	
	private ClientRunnable clientRunnable;
	
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		clientRunnable = new ClientRunnable(networkConnectionMock);
		clientRunnable.setFuture(futureMock);
	
	}

	@Test
	public void testRun_whenMessageHasUserName() {
		
		when(networkConnectionMock.iterator()).thenReturn(messageIterMock);
		when(messageIterMock.hasNext()).thenReturn(true);
		when(messageIterMock.next()).thenReturn(messageMock);
		//when(messageMock.getName()).thenReturn("testName");
		clientRunnable.run();
		assertEquals("testName", clientRunnable.getName());
		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
	}
	
	@Test
	public void testRun_whenMessageDoesNotHaveUserName() {
		when(networkConnectionMock.iterator()).thenReturn(messageIterMock);
		when(messageIterMock.hasNext()).thenReturn(true);
		when(messageIterMock.next()).thenReturn(messageMock);
		//when(messageMock.getName()).thenReturn(null);
		clientRunnable.run();
		assertEquals(null, clientRunnable.getName());
		assertEquals(-1, clientRunnable.getUserId());
		assertEquals(false, clientRunnable.isInitialized());
	}
	
	@Test
	public void testRun_whenMessageIteratorReturnsFalse() {
		when(networkConnectionMock.iterator()).thenReturn(messageIterMock);
		when(messageIterMock.hasNext()).thenReturn(false);
		clientRunnable.run();
		assertEquals(null, clientRunnable.getName());
		assertEquals(0, clientRunnable.getUserId());
	}
	
	@Test
	public void testRun_whenMessageIsTerminate() {
		initClient();
		assertEquals("testName", clientRunnable.getName());
		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		
		//when(messageMock.terminate()).thenReturn(true);
		clientRunnable.run();
		//clientRunnable.run();
		verify(networkConnectionMock, Mockito.times(1)).close();
		verify(futureMock, Mockito.atLeastOnce()).cancel(false);
		ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		verify(networkConnectionMock, Mockito.times(1)).sendMessage(messageArgumentCaptor.capture());
		Message msgSent = messageArgumentCaptor.getValue();
		assertEquals(null, msgSent.getText());
	}
	
	@Test
	public void testRun_whenMessageIsBroadcast() {
		initClient();
		assertEquals("testName", clientRunnable.getName());
		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		//when(messageMock.terminate()).thenReturn(false);
		//when(messageMock.isBroadcastMessage()).thenReturn(true);
		clientRunnable.run();
		clientRunnable.run();
	}
	
	
	@Test
	public void testRun_whenMessageIsNotBroadcast() {
		initClient();
		assertEquals("testName", clientRunnable.getName());
		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		//when(messageMock.terminate()).thenReturn(false);
		//when(messageMock.isBroadcastMessage()).thenReturn(false);
		clientRunnable.run();
		verify(networkConnectionMock, Mockito.times(0)).sendMessage(Mockito.any(Message.class));
	}
	
	@Test
	public void testRun_whenMessageIsIllegal_NameNotMatch() {
		initClient();
		assertEquals("testName", clientRunnable.getName());
		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		//when(messageMock.terminate()).thenReturn(false);
		//when(messageMock.isBroadcastMessage()).thenReturn(true);
		//when(messageMock.getName()).thenReturn("testName2");
		clientRunnable.run();
		ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		verify(networkConnectionMock, Mockito.times(1)).sendMessage(messageArgumentCaptor.capture());
		Message msgSent = messageArgumentCaptor.getValue();
		assertEquals("Last message was rejected because it specified an incorrect user name.",
				msgSent.getText());
	}
	
	@Test
	public void testRun_whenMessageIsIllegal_NameIsNull() {
		initClient();
		assertEquals("testName", clientRunnable.getName());
		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		//when(messageMock.terminate()).thenReturn(false);
		//when(messageMock.isBroadcastMessage()).thenReturn(true);
		//when(messageMock.getName()).thenReturn(null);
		clientRunnable.run();
		ArgumentCaptor<Message> messageArgumentCaptor = ArgumentCaptor.forClass(Message.class);
		verify(networkConnectionMock, Mockito.times(1)).sendMessage(messageArgumentCaptor.capture());
		Message msgSent = messageArgumentCaptor.getValue();
		assertEquals("Last message was rejected because it specified an incorrect user name.",
				msgSent.getText());
	}
	
	@Test
	public void testRun_whenIncomingMessageIsEmpty() {
		initClient();
		assertEquals("testName", clientRunnable.getName());
		assertEquals(clientRunnable.hashCode(), clientRunnable.getUserId());
		when(messageIterMock.hasNext()).thenReturn(false);
		clientRunnable.run();
		verify(networkConnectionMock, Mockito.times(0)).sendMessage(Mockito.any(Message.class));
	}
	
	private void initClient() {
		when(networkConnectionMock.iterator()).thenReturn(messageIterMock);
		when(messageIterMock.hasNext()).thenReturn(true);
		when(messageIterMock.next()).thenReturn(messageMock);
		//when(messageMock.getName()).thenReturn("testName");
		clientRunnable.run();
	}
	
	
}
