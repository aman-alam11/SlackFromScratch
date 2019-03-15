package edu.northeastern.ccs.im;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.client.communication.Connection;
import edu.northeastern.ccs.im.client.communication.SocketConnection;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SocketConnectionTest {

  private SocketConnection socketConnection;
  private static SocketChannel clientChannelOnServer;
  private static ServerSocketChannel serverSocket;
  
  @BeforeClass
  public static void startServer() throws IOException {
    serverSocket = ServerSocketChannel.open();
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
                  clientChannelOnServer = serverSocket.accept();
                }
              }
            } catch (IOException e) {
              e.printStackTrace();
              break;
            }
          }
        }
      };
      Thread serverThread = new Thread(server);
      serverThread.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  } 

  @Before
  public void init() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<?> constructor = SocketConnection.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    socketConnection = (SocketConnection) constructor.newInstance();
  }
  
  @Test
  public void testA_BeforeConnectionInitialised() {
    try {
      socketConnection.isConnected();
    } catch (Exception e) {
      assertNull(e.getMessage());
    }


    try {
      assertFalse(socketConnection.hasNext());
    } catch (Exception e) {
      assertNull(e.getMessage());
    }


    try {
      assertNull(socketConnection.next());
    } catch (Exception e) {
      assertNull(e.getMessage());
    }

    try {
      socketConnection.sendMessage(new MessageJson(MessageType.HELLO));
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }

  
  @Test
  public void testGetInstance() throws InterruptedException {
  	synchronized (this) {
  		wait(1000);
		}
  	Connection c = SocketConnection.getInstance("localhost", 4444);
  	assertNotNull(c);
  	
  }
  
  
  @Test
  public void testReceiveMessage() throws InterruptedException {
  	MessageJson msg = new MessageJson("", MessageType.HELLO, "hello");
  	Gson gson = new Gson();
  	String msgToSend = "#" + gson.toJson(msg) + "#";
  	writeToChannel(clientChannelOnServer, msgToSend);
  	synchronized (this) {
  		wait(1000);
		}
  	boolean b= SocketConnection.getInstance("localhost", 4444).hasNext();
  	MessageJson msgReceived = SocketConnection.getInstance("localhost", 4444).next();
  	assertTrue(b);
  	assertEquals("hello", msgReceived.getMessage());
  }
  
  @Test
  public void testReceiveMalFormedMessage() throws InterruptedException {
  	String msgToSend = "#" + "{name:Hello World}" + "#";
  	writeToChannel(clientChannelOnServer, msgToSend);
  	synchronized (this) {
  		wait(1000);
		}
  	boolean b= SocketConnection.getInstance("localhost", 4444).hasNext();
  	MessageJson msgReceived = SocketConnection.getInstance("localhost", 4444).next();
  	assertFalse(b);
  	assertNull(msgReceived);
  }
  
  @Test
  public void testSendMessage() {
  	MessageJson msg = new MessageJson("", MessageType.HELLO, "hello");
  	boolean b= SocketConnection.getInstance("localhost", 4444).sendMessage(msg);
  	assertTrue(b);
  }
  
  @Test
  public void test2() throws NoSuchMethodException {
    socketConnection.registerListener(message -> {
    }, MessageType.HELLO);
    Method decodeMessageMethod = SocketConnection.class.getDeclaredMethod("decodeMessage");
    decodeMessageMethod.setAccessible(true);
    try {
      decodeMessageMethod.invoke(socketConnection);
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }

  @Test
  public void test3() throws NoSuchMethodException {
    Method decodeMessageMethod = SocketConnection.class.getDeclaredMethod("initThread");
    decodeMessageMethod.setAccessible(true);
    try {
      decodeMessageMethod.invoke(socketConnection);
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }
  
  private void writeToChannel(SocketChannel channel, String message) {
  	ByteBuffer b = ByteBuffer.wrap(message.getBytes());
    while (b.hasRemaining()) {
      try {
        channel.write(b);
      } catch (IOException e) {
      	e.printStackTrace();
      }
    }
  }


}
