package edu.northeastern.ccs.im;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NetworkConnectionTest {

	private NetworkConnection netConn;
	
	private static SocketChannel serverSocketChannel;
	private SocketChannel clientSocket;
	
	@Mock
	private SelectableChannel selectableChannelMock;
	
	
	@BeforeClass
	public static void startServer() throws IOException {
		ServerSocketChannel serverSocket = ServerSocketChannel.open();
		try {
			serverSocket.configureBlocking(false);
			serverSocket.bind(new InetSocketAddress(4444));
			Selector selector = SelectorProvider.provider().openSelector();
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);

			Runnable server = new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							while (selector.select(50) != 0) {
								Set<SelectionKey> acceptKeys = selector.selectedKeys();
								Iterator<SelectionKey> it = acceptKeys.iterator();
								while (it.hasNext()) {
									SelectionKey key = it.next();
									it.remove();
									serverSocketChannel = serverSocket.accept();

								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						;
					}

				}

			};
			Thread serverThread = new Thread(server);
			serverThread.start();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_sendMessage() throws IOException, InterruptedException {
		synchronized (this) {
			
			wait(1000);
		}
		InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
		clientSocket = SocketChannel.open(hostAddress);
		netConn = new NetworkConnection(clientSocket);
		boolean b = netConn.sendMessage(Message.makeHelloMessage("Hello"));
		assertEquals(true, b);
	}
}
