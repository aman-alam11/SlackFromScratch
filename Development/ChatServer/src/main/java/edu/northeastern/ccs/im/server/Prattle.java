package edu.northeastern.ccs.im.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.ClientState;
import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.server.business.logic.JsonMessageHandlerFactory;

/**
 * A network server that communicates with IM clients that connect to it. This version of the server
 * spawns a new thread to handle each client that connects to it. At this point, messages are
 * broadcast to all of the other clients. It does not send a response when the user has gone
 * off-line.
 *
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0 International
 * License. To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/4.0/. It
 * is based on work originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 *
 * @version 1.3
 */
public abstract class Prattle {

  /**
   * Don't do anything unless the server is ready.
   */
  private static boolean isReady = false;

  @SuppressWarnings({"squid:S1450"})
  private static SelectionKey key;

  /**
   * Collection of users connected as threads that are currently not authenticated.
   */
  private static Set<ClientRunnable> unAuthenticatedActiveUsers;
  
  /**
   * Collection of users connected as threads that are currently authenticated  with key as userName.
   */
  private static ConcurrentHashMap<String, Connection> authenticatedActiveUsers;

  /**
   * Collection of users currently in chat window and chatting.
   */
  private static Set<String> chattingUsers;

  /**
   *  All of the static initialization occurs in this "method".
   */
  static {
    // Create the new queue of active threads.
    unAuthenticatedActiveUsers = Collections.newSetFromMap(new ConcurrentHashMap<ClientRunnable, Boolean>());
    authenticatedActiveUsers = new ConcurrentHashMap<>();
    chattingUsers = new HashSet<>();
  }



  /**
   * Broadcast a given message to all the other IM clients currently on the system. This message
   * _will_ be sent to the client who originally sent it.
   *
   * @param message Message that the client sent.
   */
  public static void broadcastMessage(MessageJson message) {
    // Loop through all of our active threads
    for (ClientRunnable tt : unAuthenticatedActiveUsers) {
      // Do not send the message to any clients that are not ready to receive it.
      if (tt.isAuthenticated()) {
        tt.enqueueMessage(message);
      }
    }
  }
  
  public static boolean sendMessageTo(String userName, MessageJson msg) {
	  boolean isSuccessfull = false;
	  if (authenticatedActiveUsers.containsKey(userName)) {
		  authenticatedActiveUsers.get(userName).enqueueMessage(msg);
		  isSuccessfull = true;
	  }
	  return isSuccessfull;
  }
  
  public static boolean isUserOnline(String userName) {
	  return authenticatedActiveUsers.containsKey(userName);
  }

  public static boolean isUserChatting(String userName) {
    return chattingUsers.contains(userName);
  }

  public static void addToChattingUsers(String userName) {
    chattingUsers.add(userName);
  }

  public static void removeFromChattingUsers(String userName) {
    chattingUsers.remove(userName);
  }

 /**
  *  This method is used to change an unauthenticated user thread to authenticated
  * @param clientThread
  * @param userName
  * @return true if succeeded else false
  */
  public static boolean changeToAuthenciatedUser(ClientRunnable clientThread, String userName) {
		synchronized (Prattle.class) {
			if (unAuthenticatedActiveUsers.contains(clientThread)) {
				unAuthenticatedActiveUsers.remove(clientThread);
				authenticatedActiveUsers.put(userName, clientThread);
				clientThread.setState(ClientState.LOGGED_IN);
				return true;
			} else {
				return false;
			}
		}
  }

  /**
   * Remove the given IM client from the list of active threads.
   *
   * @param client Thread which had been handling all the I/O for a client who has since quit.
   */
  public static void removeClient(Connection client) {
    // Test and see if the thread was in our list of active clients so that we
    // can remove it.
	if (client.isAuthenticated() && authenticatedActiveUsers.containsKey(client.getUserName())) {
		Connection c = authenticatedActiveUsers.remove(client);
		ChatLogger.info("Terminated authenticated client " + c.getUserName());
		
	} else if (unAuthenticatedActiveUsers.contains(client)) {
		unAuthenticatedActiveUsers.remove(client);
		ChatLogger.info("Terminated unauthenticated client ");
	} else {
		ChatLogger.info("Could not find a thread that I tried to remove!\n");
	}
  }


  /**
   * Terminates the server.
   */
  public static void stopServer() {
    isReady = false;
  }

  /**
   * Start up the threaded talk server. This class accepts incoming connections on a specific port
   * specified on the command-line. Whenever it receives a new connection, it will spawn a thread to
   * perform all of the I/O with that client. This class relies on the server not receiving too many
   * requests -- it does not include any code to limit the number of extant threads.
   *
   * @param args String arguments to the server from the command line. At present the only legal
   *             (and required) argument is the port on which this server should list.
   * @throws IOException Exception thrown if the server cannot connect to the port to which it is
   *                     supposed to listen.
   */
  public static void main(String[] args) {
    // Connect to the socket on the appropriate port to which this server connects.
    try (ServerSocketChannel serverSocket = ServerSocketChannel.open()) {
      serverSocket.configureBlocking(false);
      serverSocket.socket().bind(new InetSocketAddress(ServerConstants.PORT));
      // Create the Selector with which our channel is registered.
      Selector selector = SelectorProvider.provider().openSelector();
      // Register to receive any incoming connection messages.
      serverSocket.register(selector, SelectionKey.OP_ACCEPT);

      //Get number of logical cores.
      int cores = Runtime.getRuntime().availableProcessors();
      // Create our pool of threads on which we will execute.
      ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(cores*4);
      // If we get this far than the server is initialized correctly
      isReady = true;
      // Now listen on this port as long as the server is ready
      while (isReady) {
        // Check if we have a valid incoming request, but limit the time we may wait.
        while (selector.select(ServerConstants.DELAY_IN_MS) != 0) {
          // Get the list of keys that have arrived since our last check
          Set<SelectionKey> acceptKeys = selector.selectedKeys();
          // Now iterate through all of the keys
          Iterator<SelectionKey> it = acceptKeys.iterator();
          while (it.hasNext()) {
            // Get the next key, it had better be from a new incoming connection
            key = it.next();
            it.remove();
            // Assert certain things I really hope is true
            assert key.isAcceptable();
            assert key.channel() == serverSocket;
            // Create new thread to handle client for which we just received request.
            createClientThread(serverSocket, threadPool);
          }
        }
      }
    } catch (Exception ex) {
      ChatLogger.error("Fatal error: " + ex.getMessage());
      throw new IllegalStateException(ex.getMessage());
    }
  }

  /**
   * Create a new thread to handle the client for which a request is received.
   *
   * @param serverSocket The channel to use.
   * @param threadPool   The thread pool to add client to.
   */
  @SuppressWarnings({"squid:S00112"})
  private static void createClientThread(ServerSocketChannel serverSocket, ScheduledExecutorService threadPool) {
    try {
      if (threadPool == null) {
        ChatLogger.error("Thread Pool is null");
        throw new Exception("Thread Pool and server socket are both null");
      }
      // Accept the connection and create a new thread to handle this client.
      SocketChannel socket = serverSocket.accept();
      // Make sure we have a connection to work with.
      NetworkConnection connection = new NetworkConnection(socket, new JsonMessageHandlerFactory());
      ClientRunnable tt = new ClientRunnable(connection);
      // Add the thread to the queue of active threads
      unAuthenticatedActiveUsers.add(tt);
      // Have the client executed by our pool of threads.
      ScheduledFuture<?> clientFuture = threadPool.scheduleAtFixedRate(tt, ServerConstants.CLIENT_CHECK_DELAY,
              ServerConstants.CLIENT_CHECK_DELAY, TimeUnit.MILLISECONDS);
      tt.setFuture(clientFuture);
    } catch (Exception e) {
      ChatLogger.error("Caught Exception: " + e.toString());
    }
  }
}