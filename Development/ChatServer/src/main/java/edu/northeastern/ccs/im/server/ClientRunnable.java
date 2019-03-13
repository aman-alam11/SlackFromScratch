package edu.northeastern.ccs.im.server;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.ClientState;
import edu.northeastern.ccs.im.Message;
import edu.northeastern.ccs.im.NetworkConnection;
import edu.northeastern.ccs.im.business.logic.MessageHandler;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;
import edu.northeastern.ccs.im.model.LoginCredentials;

/**
 * Instances of this class handle all of the incoming communication from a
 * single IM client. Instances are created when the client signs-on with the
 * server. After instantiation, it is executed periodically on one of the
 * threads from the thread pool and will stop being run only when the client
 * signs off.
 * 
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 * 
 * @version 1.3
 */
public class ClientRunnable implements Connection {
	/**
	 * Utility class which we will use to send and receive communication to this
	 * client.
	 */
	private NetworkConnection connection;

	/** Name that the client used when connecting to the server. */
	private String userName;

	/**
	 * Whether this client has been initialized, set its user name, and is ready to
	 * receive messages.
	 */
	private boolean initialized;

	/**
	 * Whether this client has been terminated, either because he quit or due to
	 * prolonged inactivity.
	 */
	private Boolean terminate;

	/** The timer that keeps track of the clients activity. */
	private ClientTimer timer;

	/**
	 * The future that is used to schedule the client for execution in the thread
	 * pool.
	 */
	private ScheduledFuture<?> runnableMe;

	/** Collection of messages queued up to be sent to this client. */
	private Queue<MessageJson> waitingList;
	
	/** This enum holds the state, whether user is logged in or not */
	private ClientState state;
	
	private Iterator<MessageJson> messageIter;
	
	/**
	 * Create a new thread with which we will communicate with this single client.
	 * 
	 * @param network NetworkConnection used by this new client
	 */
	public ClientRunnable(NetworkConnection network) {
		// Create the class we will use to send and receive communication
		connection = network;
		// Mark that we are not initialized
		initialized = false;
		// Mark that we are not terminated
		terminate = false;
		// Create the queue of messages to be sent
		waitingList = new ConcurrentLinkedQueue<>();
		// Mark that the client is active now and start the timer until we
		// terminate for inactivity.
		timer = new ClientTimer();
		// not logged in initially
		state = ClientState.LOGGED_OUT;
		//Message iterator
		messageIter = connection.iterator();
	}

	/**
	 * Check if the message is properly formed. At the moment, this means checking
	 * that the identifier is set properly.
	 * 
	 * @param msg Message to be checked
	 * @return True if message is correct; false otherwise
	 */
	private boolean messageChecks(Message msg) {
		// Check that the message name matches.
		return (msg.getName() != null) && (msg.getName().compareToIgnoreCase(getUserName()) == 0);
	}

	/**
	 * Immediately send this message to the client. This returns if we were
	 * successful or not in our attempt to send the message.
	 * 
	 * @param message Message to be sent immediately.
	 * @return True if we sent the message successfully; false otherwise.
	 */
	private boolean sendMessage(MessageJson message) {
		ChatLogger.info("\t" + message);
		return connection.sendMessage(message);
	}

	/**
	 * Try allowing this user to set his/her user name to the given username.
	 * 
	 * @param userName The new value to which we will try to set userName.
	 * @return True if the username is deemed acceptable; false otherwise
	 */
	private boolean setUserName(String userName) {
		boolean result = false;
		// Now make sure this name is legal.
		if (userName != null) {
			// Optimistically set this users ID number.
			setName(userName);
			result = true;
		} else {
			// Clear this name; we cannot use it. *sigh*
			result = false;
		}
		return result;
	}

	/**
	 * Add the given message to this client to the queue of message to be sent to
	 * the client.
	 * 
	 * @param message Complete message to be sent.
	 */
	public void enqueueMessage(MessageJson message) {
		waitingList.add(message);
	}

	/**
	 * Get the name of the user for which this ClientRunnable was created.
	 * 
	 * @return Returns the name of this client.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Set the name of the user for which this ClientRunnable was created.
	 * 
	 * @param name The name for which this ClientRunnable.
	 */
	private void setName(String name) {
		this.userName = name;
	}

	/**
	 * Return if this thread has been authenticated, ie user has logged in
	 * 
	 * @return True if this corresponding client has logged in; false otherwise.
	 */
	public boolean isAuthenticated() {
		return state.equals(ClientState.LOGGED_IN);
	}

	/**
	 * Perform the periodic actions needed to work with this client.
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		if (state.equals(ClientState.LOGGED_OUT)) {
			//handle unauthenticated message
			handleUnauthIncomingMessages();
			//handleUnauthOutgoingMessages();
		} else {
			handleIncomingMessages();
		}
		handleOutgoingMessages();
		//}
		// Finally, check if this client have been inactive for too long and,
		// when they have, terminate the client.
		if (timer.isBehind()) {
			ChatLogger.error("Timing out or forcing off a user " + userName);
			terminate = true;
		}

		if (terminate) {
			terminateClient();
		}
	}
	
	private void handleUnauthIncomingMessages() {
		if (messageIter.hasNext()) {
			MessageJson msg = messageIter.next();
			MessageHandler msgHandler = connection.getMessageHandlerFactory().getMessageHandler(msg.getMessageType());
			boolean response = msgHandler.handleMessage("", msg.getMessage(), this);
			if(msg.getMessageType().equals(MessageType.LOGIN) && response) {
				//change to authenticated user
				LoginCredentials lc = new Gson().fromJson(msg.getMessage(), LoginCredentials.class);
				signInUser(lc.getUserName());
			}
		}
	}
	
	private void signInUser(String userName) {
		setUserName(userName);
		Prattle.changeToAuthenciatedUser(this, userName);
		ChatLogger.info("User Logged in! " + userName + "\n");
	}

	/**
	 * Checks incoming messages and performs appropriate actions based on the type
	 * of message.
	 */
	private void handleIncomingMessages() {
		// Client has already been logged in
		if (messageIter.hasNext()) {
			MessageJson msg = messageIter.next();
			if (msg.getMessageType().equals(MessageType.LOG_OUT)) {
				terminate = true;
			}

			MessageHandler messageHandler = connection.getMessageHandlerFactory()
					.getMessageHandler(msg.getMessageType());
			messageHandler.handleMessage(userName, msg.getMessage(), this);
		}
	}

	/**
	 * Sends the enqueued messages to the printer and makes sure they were sent out.
	 */
	private void handleOutgoingMessages() {
		// Check to make sure we have a client to send to.
		boolean keepAlive = true;
		if (!waitingList.isEmpty()) {
			keepAlive = false;
			// Send out all of the message that have been added to the
			// queue.
			do {
				MessageJson msg = waitingList.remove();
				boolean sentGood = sendMessage(msg);
				keepAlive |= sentGood;
				// Update the time until we terminate the client for inactivity.
				timer.updateAfterActivity();

			} while (!waitingList.isEmpty());
		}
		terminate |= !keepAlive;
	}

	/**
	 * Store the object used by this client runnable to control when it is scheduled
	 * for execution in the thread pool.
	 * 
	 * @param future Instance controlling when the runnable is executed from within
	 *               the thread pool.
	 */
	public void setFuture(ScheduledFuture<?> future) {
		runnableMe = future;
	}

	/**
	 * Terminate a client that we wish to remove. This termination could happen at
	 * the client's request or due to system need.
	 */
	public void terminateClient() {
		// Once the communication is done, close this connection.
		connection.close();
		ChatLogger.info("Logged out! " + userName + "\n");
		// Remove the client from our client listing.
		Prattle.removeClient(this);
		// And remove the client from our client pool.
		runnableMe.cancel(false);
	}
	
	public ClientState getState() {
		return state;
	}

	public void setState(ClientState state) {
		this.state = state;
	}
}