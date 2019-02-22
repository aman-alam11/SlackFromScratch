package edu.northeastern.ccs.im.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.northeastern.ccs.im.Message;
import junit.framework.Assert;

public class PrattleTest {
	
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
		Prattle.stopServer();
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
		}catch (Exception e) {
			Assert.fail();
		}
	}
	
	
}
