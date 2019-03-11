package edu.northeastern.ccs.im.server;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.business.logic.JsonMessageHandlerFactory;

import junit.framework.Assert;

public class PrattleTest {

	@Mock
	JsonMessageHandlerFactory messageHandlerFactory;

	@BeforeClass
	public static void startServer() {
		Runnable server = new Runnable() {

			@Override
			public void run() {
				Prattle.main(new String[0]);
			}
		};
		Thread serverThread = new Thread(server);
		serverThread.start();
	}

	@Test
	public void testMain() {
		try {
			synchronized (this) {

				wait(1000);
			}
			InetSocketAddress hostAddress = new InetSocketAddress("localhost", ServerConstants.PORT);
			SocketChannel client = SocketChannel.open(hostAddress);

			synchronized (this) {

				wait(1000);
			}
			client.close();
			synchronized (this) {

				wait(1000);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			Assert.fail();
			Prattle.stopServer();

		}
	}

	@Test
	public void testBroadcast_WithNoActiveClients() {
		try {

			Prattle.broadcastMessage(Message.makeBroadcastMessage("kumar", "test"));
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testRemoveAlternate() throws Exception {
		synchronized (this) {

			wait(1000);
		}
		InetSocketAddress hostAddress = new InetSocketAddress("localhost", ServerConstants.PORT);
		SocketChannel client = SocketChannel.open(hostAddress);
		ClientRunnable runnable = new ClientRunnable(new NetworkConnection(client, messageHandlerFactory));
		Field activeField = Prattle.class.getDeclaredField("active");
		activeField.setAccessible(true);
		java.util.concurrent.ConcurrentLinkedQueue<ClientRunnable> queue = 
				(java.util.concurrent.ConcurrentLinkedQueue<ClientRunnable>) activeField.get(new Prattle() {
				});
		queue.add(runnable);
		Prattle.removeClient(runnable);
	}
	
	@AfterClass
	public static void tearDown() {
		Prattle.stopServer();
	}
}
