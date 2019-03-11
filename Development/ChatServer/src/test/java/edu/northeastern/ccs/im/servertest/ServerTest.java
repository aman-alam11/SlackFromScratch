package edu.northeastern.ccs.im.servertest;


import static edu.northeastern.ccs.im.server.Prattle.broadcastMessage;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.server.ClientRunnable;
import edu.northeastern.ccs.im.server.ClientTimer;
import edu.northeastern.ccs.im.server.Prattle;

public class ServerTest {
  private static SocketChannel clientSocket;

  @Test
  public void test() throws Exception {
    ClientTimer timer = new ClientTimer();

    Field constantField = ClientTimer.class.getDeclaredField("TERMINATE_AFTER_INACTIVE_INITIAL_IN_MS");
    constantField.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(constantField, constantField.getModifiers() & ~Modifier.FINAL);
    constantField.set(null, -2000000000);
    timer.updateAfterInitialization();

    Field calendarField = ClientTimer.class.getDeclaredField("calendar");
    calendarField.setAccessible(true);
    calendarField.set(timer, null);

    ClientRunnable runnable = new ClientRunnable(new NetworkConnection(clientSocket));
    runnable.run();
  }


  @BeforeClass
  public static void init() {

    // Initialize All Tests params
    try {
      new Thread(() -> Prattle.main(null)).start();
    } catch (Exception e) {
      Logger.getLogger(e.getMessage());
    }

    synchronized (ServerTest.class) {
        try {
        	ServerTest.class.wait(1000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

    InetSocketAddress hostAddress = new InetSocketAddress("localhost", 4545);
    try {
      clientSocket = SocketChannel.open(hostAddress);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  @Test
  public void testBroadcastMessageBranch() throws NoSuchFieldException, IllegalAccessException {
    Field activeField = Prattle.class.getDeclaredField("active");
    activeField.setAccessible(true);
    ConcurrentLinkedQueue<ClientRunnable> queue =
            (ConcurrentLinkedQueue<ClientRunnable>) activeField.get(new Prattle() {
            });

    ClientRunnable runnable = new ClientRunnable(new NetworkConnection(clientSocket));
    Field initField = ClientRunnable.class.getDeclaredField("initialized");
    initField.setAccessible(true);
    initField.set(runnable, true);
    queue.add(runnable);
    broadcastMessage(Message.makeBroadcastMessage("NAME", "HEY WASSUP"));
  }


  @Test
  public void testRemove() {
    Prattle.removeClient(null);
  }

  @Test
  public void testClientThreadCreation() throws NoSuchMethodException, InvocationTargetException,
          IllegalAccessException {
    Method clientMethod = Prattle.class.getDeclaredMethod("createClientThread",
            ServerSocketChannel.class, ScheduledExecutorService.class);
    clientMethod.setAccessible(true);
    clientMethod.invoke(new Prattle() {
    }, null, null);
  }


  @Test
  public void testKeyNull() throws NoSuchFieldException, IllegalAccessException, IOException {
    Field keyField = Prattle.class.getDeclaredField("key");
    keyField.setAccessible(true);
    SelectionKey key = (SelectionKey) keyField.get(new Prattle() {
    });
    key.cancel();

    new Thread(() -> Prattle.main(null)).start();
  }


  @AfterClass
  public static void endServer() {
    Prattle.stopServer();
  }

}
