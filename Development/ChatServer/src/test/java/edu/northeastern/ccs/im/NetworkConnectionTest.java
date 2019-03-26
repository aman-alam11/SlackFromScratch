package edu.northeastern.ccs.im;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.server.business.logic.JsonMessageHandlerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("all")
public class NetworkConnectionTest {

  private NetworkConnection netConn;

  private static SocketChannel serverSocketChannel;
  private SocketChannel clientSocket;


  @Mock
  JsonMessageHandlerFactory messageHandlerFactory;

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
          }
        }
      };
      Thread serverThread = new Thread(server);
      serverThread.start();
    } catch (Exception e) {
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
    netConn = new NetworkConnection(clientSocket, messageHandlerFactory);
    MessageJson messageJson = new GenerateLoginCredentials().generateLoginCredentials("user",
            "pass",
            MessageType.LOGIN);
    boolean b = netConn.sendMessage(messageJson);
    assertTrue(b);
  }


  @Test
  public void test_sendMessage_empty() throws IOException, InterruptedException {
    synchronized (this) {

      wait(1000);
    }
    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
    clientSocket = SocketChannel.open(hostAddress);
    netConn = new NetworkConnection(clientSocket, messageHandlerFactory);
    MessageJson messageJson = new GenerateLoginCredentials().generateLoginCredentials("user",
            "pass",
            MessageType.LOGIN);
    boolean b = netConn.sendMessage(messageJson);
    assertTrue(b);
  }

//  @Test
//  public void test_iterator() throws IOException, InterruptedException {
//    synchronized (this) {
//
//      wait(1000);
//    }
//    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
//    clientSocket = SocketChannel.open(hostAddress);
//    netConn = new NetworkConnection(clientSocket, messageHandlerFactory);
//    String str = Message.makeHelloMessage("hellow world").toString();
//    ByteBuffer wrapper = ByteBuffer.wrap(str.getBytes());
//    serverSocketChannel.write(wrapper);
//    synchronized (this) {
//      wait(1000);
//    }
//    Iterator<MessageJson> messageItr = netConn.iterator();
//
//    //serverSocketChannel.write(src)
//    while (messageItr.hasNext()) {
//      //System.out.println(messageItr.next().getText());
//      messageItr.next();
//    }
//    MessageJson messageJson = new GenerateLoginCredentials().generateLoginCredentials("user",
//            "pass",
//            MessageType.LOGIN);
//    boolean b = netConn.sendMessage(messageJson);
//    netConn.close();
//    assertTrue(b);
//  }


  @Test
  public void test_sendMessage_exception() throws IOException, InterruptedException {
    synchronized (this) {

      wait(1000);
    }
    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
    clientSocket = SocketChannel.open(hostAddress);
    netConn = new NetworkConnection(clientSocket, messageHandlerFactory);
    String str = Message.makeHelloMessage("hellow world").toString();
    ByteBuffer wrapper = ByteBuffer.wrap(str.getBytes());
    serverSocketChannel.write(wrapper);
    synchronized (this) {
      wait(1000);
    }
    Iterator<MessageJson> messageItr = netConn.iterator();
    clientSocket.close();
    //serverSocketChannel.write(src)
    while (messageItr.hasNext()) {
      //System.out.println(messageItr.next().getText());
      messageItr.next();
    }
    MessageJson messageJson = new GenerateLoginCredentials().generateLoginCredentials("user",
            "pass",
            MessageType.LOGIN);
		boolean b = netConn.sendMessage(messageJson);
    netConn.close();
		assertFalse(b);
  }

  @Test
  public void test_sendMalformedMessage() throws IOException, InterruptedException {
    synchronized (this) {

      wait(1000);
    }
    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
    clientSocket = SocketChannel.open(hostAddress);
    netConn = new NetworkConnection(clientSocket, messageHandlerFactory);
    String str = Message.makeHelloMessage("hellow world").toString();
    ByteBuffer wrapper = ByteBuffer.wrap(longString.getBytes());
    serverSocketChannel.write(wrapper);
    synchronized (this) {

      wait(1000);
    }
    Iterator<MessageJson> messageItr = netConn.iterator();
    clientSocket.close();
    //serverSocketChannel.write(src)
    while (messageItr.hasNext()) {
      //System.out.println(messageItr.next().getText());
      messageItr.next();
    }
    MessageJson messageJson = new GenerateLoginCredentials().generateLoginCredentials("user",
            "pass",
            MessageType.LOGIN);
		boolean b = netConn.sendMessage(messageJson);
    netConn.close();
		assertFalse(b);
  }

  @Test(expected = NoSuchElementException.class)
  public void test_iterator_error() throws IOException, InterruptedException {
    synchronized (this) {

      wait(1000);
    }
    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
    clientSocket = SocketChannel.open(hostAddress);
    netConn = new NetworkConnection(clientSocket, messageHandlerFactory);
    //String str= Message.makeHelloMessage("hellow world").toString();
    //ByteBuffer wrapper = ByteBuffer.wrap(str.getBytes());
    //serverSocketChannel.write(wrapper);
    Iterator<MessageJson> messageItr = netConn.iterator();

    //serverSocketChannel.write(src)
    while (messageItr.hasNext()) {
      //System.out.println(messageItr.next().getText());
      messageItr.next();
    }
//		boolean b = netConn.sendMessage(Message.makeHelloMessage(""));
    messageItr.next();
    netConn.close();
//		assertEquals(true, b);
  }

  @Test
  public void test_sendMessage_exceeded_retry() throws IOException, InterruptedException {
    synchronized (this) {

      wait(1000);
    }
    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
    clientSocket = SocketChannel.open(hostAddress);
    netConn = new NetworkConnection(clientSocket, messageHandlerFactory);

    Field maxTries;
    try {
      maxTries = NetworkConnection.class.getDeclaredField("MAXIMUM_TRIES_SENDING");
      maxTries.setAccessible(true);
      int maxTriesField = (int) maxTries.get(netConn);
      maxTriesField = 1;
    } catch (Exception e) {
      e.printStackTrace();
    }

//		boolean b = netConn.sendMessage(Message.makeHelloMessage(""));
//		assertEquals(true, b);

  }

  @Test
  public void test1() throws Exception {
    synchronized (this) {
      wait(1000);
    }
    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
    final SocketChannel clientSocket2 = SocketChannel.open(hostAddress);
    clientSocket2.close();
    netConn = new NetworkConnection(clientSocket2, messageHandlerFactory);
  }


  @Test
  public void test2() throws Exception {
    synchronized (this) {
      wait(1000);
    }
    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
    SocketChannel clientSocket2 = SocketChannel.open(hostAddress);
    String str = Message.makeHelloMessage("hellow world").toString();
    ByteBuffer wrapper = ByteBuffer.wrap(str.getBytes());
    netConn = new NetworkConnection(clientSocket2, messageHandlerFactory);
    netConn.close();
  }


  @Test(expected = AssertionError.class)
  public void coverMoreBranchesNetworkConnection() throws IOException {
    try {
      InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
      clientSocket = SocketChannel.open(hostAddress);
      NetworkConnection connection = new NetworkConnection(clientSocket, messageHandlerFactory);
      Field selectorField = NetworkConnection.class.getDeclaredField("selector");
      selectorField.setAccessible(true);
      Selector selector = (Selector) selectorField.get(connection);
      selectorField.set(connection, null);
      connection.close();
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testEmptyIterator() throws NoSuchFieldException, IllegalAccessException, IOException {
    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4444);
    clientSocket = SocketChannel.open(hostAddress);
    NetworkConnection connection = new NetworkConnection(clientSocket, messageHandlerFactory);
    Field messageQueueField = NetworkConnection.class.getDeclaredField("messages");
    messageQueueField.setAccessible(true);
    Queue<Message> queue = (Queue<Message>) messageQueueField.get(connection);
    queue.add(Message.makeBroadcastMessage("dummy", "STRING"));
    assertTrue(connection.iterator().hasNext());
  }


  private String longString = "Based on your input, get a random alpha numeric string. The random string generator creates a series of numbers and letters that have no pattern. These can be helpful for creating security codes.\n" +
          "\n" +
          "\n" +
          "With this utility you generate a 16 character output based on your input of numbers and upper and lower case letters.  Random strings can be unique. Used in computing, a random string generator can also be called a random character string generator. This is an important tool if you want to generate a unique set of strings. The utility generates a sequence that lacks a pattern and is random.\n" +
          "\n" +
          "\n" +
          "Throughout time, randomness was generated through mechanical devices such as dice, coin flips, and playing cards. A mechanical method of achieving randomness can be more time and resource consuming especially when a large number of randomized strings are needed as they could be in statistical applications.  Computational random string generators replace the traditional mechanical devices. \n" +
          "\n" +
          "\n" +
          "Possible applications for a random string generator could be for statistical sampling, simulations, and cryptography.  For security reasons, a random string generator can be useful. \n" +
          "\n" +
          "\n" +
          "The generation of this type of random string can be a common or typical task in computer programming.  Some forms of randomness concern hash or seach algorithms.  Another task that is random concerns selecting music tracks.";

}
