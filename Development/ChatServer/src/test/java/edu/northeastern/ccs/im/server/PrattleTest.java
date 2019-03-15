package edu.northeastern.ccs.im.server;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.business.logic.JsonMessageHandlerFactory;


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

//  @Test(expected = Exception.class)
//  public void testMain() {
//    try {
//      synchronized (this) {
//
//        wait(1000);
//      }
//      InetSocketAddress hostAddress = new InetSocketAddress("localhost", ServerConstants.PORT);
//      SocketChannel client = SocketChannel.open(hostAddress);
//
//      synchronized (this) {
//
//        wait(1000);
//      }
//      client.close();
//      synchronized (this) {
//
//        wait(1000);
//      }
//    } catch (IOException | InterruptedException e) {
//      Logger.getLogger(this.getClass().getSimpleName() + ":" + e.getMessage() + " testMain");
//      Prattle.stopServer();
//    }
//  }

//  @Test(expected = Exception.class)
//  public void testBroadcast_WithNoActiveClients() {
//    try {
//      // TODO: Check
//      // Prattle.broadcastMessage(Message.makeBroadcastMessage("kumar", "test"));
//    } catch (Exception e) {
//      Logger.getLogger(this.getClass().getSimpleName() + ":" + e.getMessage()
//              + " testBroadcast_WithNoActiveClients");
//    }
//  }

//  @Test
//  public void testRemoveAlternate() throws Exception {
//    synchronized (this) {
//      wait(1000);
//    }
//    InetSocketAddress hostAddress = new InetSocketAddress("localhost", ServerConstants.PORT);
//    SocketChannel client = SocketChannel.open(hostAddress);
//    ClientRunnable runnable = new ClientRunnable(new NetworkConnection(client, messageHandlerFactory));
//    Field activeField = Prattle.class.getDeclaredField("active");
//    activeField.setAccessible(true);
//    ConcurrentLinkedQueue<ClientRunnable> queue = (ConcurrentLinkedQueue<ClientRunnable>)
//            activeField.get(new Prattle() {});
//    queue.add(runnable);
//    Prattle.removeClient(runnable);
//  }

  @AfterClass
  public static void tearDown() {
    Prattle.stopServer();
  }
}
