package edu.northeastern.ccs.im;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.northeastern.ccs.im.business.logic.MessageHandlerFactory;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;

/**
 * This class is similar to the java.io.PrintWriter class, but this class's
 * methods work with our non-blocking Socket classes. This class could easily be
 * made to wait for network output (e.g., be made &quot;non-blocking&quot; in
 * technical parlance), but I have not worried about it yet.
 * 
 * This work is licensed under the Creative Commons Attribution-ShareAlike 4.0
 * International License. To view a copy of this license, visit
 * http://creativecommons.org/licenses/by-sa/4.0/. It is based on work
 * originally written by Matthew Hertz and has been adapted for use in a class
 * assignment at Northeastern University.
 * 
 * @version 1.4
 */
public class NetworkConnection implements Iterable<edu.northeastern.ccs.im.message.Message> {

	/** The size of the incoming buffer. */
	private static final int BUFFER_SIZE = 64 * 1024;
	
	private static final String MESSAGE_START_KEY = "#{";
	
	private static final String MESSAGE_END_KEY = "}#";

	/** The base for number conversions. */
	private static final int DECIMAL_RADIX = 10;

	/** The length of the message handle. */ // MEJ: why is this not in Message?
	private static final int HANDLE_LENGTH = 3;

	/** The minimum length of a message. */ // MEJ: why is this not in Message?
	private static final int MIN_MESSAGE_LENGTH = 7;

	/** The default character set. */
	private static final String CHARSET_NAME = "us-ascii";

	/**
	 * Number of times to try sending a message before we give up in frustration.
	 */
	private static final int MAXIMUM_TRIES_SENDING = 100;

	/** Channel over which we will send and receive messages. */
	private final SocketChannel channel;

	/** Selector for this client's connection. */
	private Selector selector;

	/** Selection key for this client's connection. */
	private SelectionKey key;

	/** Byte buffer to use for incoming messages to this client. */
	private ByteBuffer buff;

	/** Queue of messages for this client. */
	private Queue<edu.northeastern.ccs.im.message.Message> messages;
	
	/** String Buffer to hold the messages, till we get the whole message */
	private StringBuilder messageBuffer;
	
	private MessageHandlerFactory messageHandlerFactory;

	public MessageHandlerFactory getMessageHandlerFactory() {
		return messageHandlerFactory;
	}

	/**
	 * Creates a new instance of this class. Since, by definition, this class sends
	 * output over the network, we need to supply the non-blocking Socket instance
	 * to which we will write.
	 * 
	 * @param sockChan Non-blocking SocketChannel instance to which we will send all
	 *                 communication.
	 * @throws IOException Exception thrown if we have trouble completing this
     *                     connection
	 */
	public NetworkConnection(SocketChannel sockChan, MessageHandlerFactory messageHandlerFactory) {
		// Create the queue that will hold the messages received from over the network
		messages = new ConcurrentLinkedQueue<>();
		// Allocate the buffer we will use to read data
		buff = ByteBuffer.allocate(BUFFER_SIZE);
		// Remember the channel that we will be using.
	   // Set up the SocketChannel over which we will communicate.
		channel = sockChan;
		
		this.messageHandlerFactory = messageHandlerFactory;
		
		messageBuffer = new StringBuilder();
		try {
			channel.configureBlocking(false);
			// Open the selector to handle our non-blocking I/O
			selector = Selector.open();
			// Register our channel to receive alerts to complete the connection
			key = channel.register(selector, SelectionKey.OP_READ);
		} catch (IOException e) {
			// For the moment we are going to simply cover up that there was a problem.
			ChatLogger.error(e.toString());
		}
	}

	/**
	 * Send a Message over the network. This method performs its actions by printing
	 * the given Message over the SocketNB instance with which the PrintNetNB was
	 * instantiated. This returns whether our attempt to send the message was
	 * successful.
	 * 
	 * @param msg Message to be sent out over the network.
	 * @return True if we successfully send this message; false otherwise.
	 */
	public boolean sendMessage(Message msg) {
		boolean result = true;
		String str = msg.toString();
		ByteBuffer wrapper = ByteBuffer.wrap(str.getBytes());
		int bytesWritten = 0;
		int attemptsRemaining = MAXIMUM_TRIES_SENDING;
		while (result && wrapper.hasRemaining() && (attemptsRemaining > 0)) {
			try {
				attemptsRemaining--;
				bytesWritten += channel.write(wrapper);
			} catch (IOException e) {
				// Show that this was unsuccessful
				result = false;
			}
		}
		// Check to see if we were successful in our attempt to write the message
		if (result && wrapper.hasRemaining()) {
			ChatLogger.warning("WARNING: Sent only " + bytesWritten + " out of " + wrapper.limit()
					+ " bytes -- dropping this user.");
			result = false;
		}
		return result;
	}

	/**
	 * Close this client network connection.
	 */
	public void close() {
		try {
			selector.close();
			channel.close();
		} catch (Exception e) {
			ChatLogger.error("Caught exception: " + e.toString());
			assert false;
		}
	}
	
	  @Override
	  public Iterator<edu.northeastern.ccs.im.message.Message> iterator() {
	    return new MessageIterator();
	  }

	  /**
	   * Private class that helps iterate over a Network Connection.
	   * 
	   * @author Riya Nadkarni
	   * @version 12-27-2018
	   */
	  private class MessageIterator implements Iterator<edu.northeastern.ccs.im.message.Message> {
		  
		  Gson gson;

	    /** Default constructor. */
	    public MessageIterator() {
	    	gson = new Gson();
	    }

	    @Override
		public boolean hasNext() {
			boolean result = false;
			try {
				// If we have messages waiting for us, return true.
				if (!messages.isEmpty()) {
					result = true;
				}
				// Otherwise, check if we can read in at least one new message
				else if (selector.selectNow() != 0) {
					assert key.isReadable();
					// Read in the next set of commands from the channel.
					int dataRead = channel.read(buff);
					//Check if client in dead
					if (dataRead < 0) {
						//Client is dead, add a logout message
						messages.add(new MessageJson(MessageType.LOG_OUT));
					} else {
						selector.selectedKeys().remove(key);
						buff.flip();
						// Create a decoder which will convert our traffic to something useful
						Charset charset = Charset.forName(CHARSET_NAME);
						CharsetDecoder decoder = charset.newDecoder();
						// Convert the buffer to a format that we can actually use.
						CharBuffer charBuffer = decoder.decode(buff);
						messageBuffer.append(charBuffer);
						// Move all of the remaining data to the start of the buffer.
						buff.compact();
					}
					result = decodeMessage();
				}
			} catch (IOException ioe) {
				// For the moment, we will cover up this exception and hope it never occurs.
				assert false;
			}
			return result;
		}
	    
	    @Override
	    public edu.northeastern.ccs.im.message.Message next() {
	      if (messages.isEmpty()) {
	        throw new NoSuchElementException("No next line has been typed in at the keyboard");
	      }
	      edu.northeastern.ccs.im.message.Message msg = messages.remove();
	      ChatLogger.info(msg.toString());
	      return msg;
	    }
	    
	    private boolean decodeMessage() {
	    	boolean isMessageDecoded = false;
	    	if (messageBuffer.length() > 0) {
	    		String msgBufferString = messageBuffer.toString();
	    		if (msgBufferString.contains(MESSAGE_START_KEY) && msgBufferString.contains(MESSAGE_END_KEY)) {
	    			int start = msgBufferString.indexOf(MESSAGE_START_KEY) + 1;
		    		int end = msgBufferString.indexOf(MESSAGE_END_KEY) + 1;
		    		String msg = msgBufferString.substring(start, end);
		    		try {
		    			//Extract message
		    			MessageJson extractedMessage = gson.fromJson(msg, MessageJson.class);
		    			isMessageDecoded  = true;
		    			messages.add(extractedMessage);
		    		} catch (JsonSyntaxException e) {
		    			ChatLogger.error(e.getMessage());
					} finally {
						//remove the particular message extracted
						messageBuffer.delete(start -1, end + 1);
					}
		    		
		    		
	    		}
	    		
	    	}
	    	return isMessageDecoded;
	    }
	    
	    /**
	     * Read in a new argument from the IM server.
	     * 
	     * @param charBuffer Buffer holding text from over the network.
	     * @return String holding the next argument sent over the network.
	     */
	    private String readArgument(CharBuffer charBuffer) {
	        String result = null;
	        // Compute the current position in the buffer
	        int pos = charBuffer.position();
	        // Compute the length of this argument
	        int length = 0;
	        // Track the number of locations visited.
	        int seen = 0;
	        // Assert that this character is a digit representing the length of the first
	        // argument
	        assert Character.isDigit(charBuffer.get(pos));
	        // Now read in the length of the first argument
	        while (Character.isDigit(charBuffer.get(pos))) {
	            // My quick-and-dirty numeric converter
	            length = length * DECIMAL_RADIX;
	            length += Character.digit(charBuffer.get(pos), DECIMAL_RADIX);
	            // Move to the next character
	            pos += 1;
	            seen += 1;
	        }
	        seen += 1;
	        if (length == 0) {
	            // Update our position
	            charBuffer.position(pos);
	        } else {
	            // Length is greater than 0 so result should be something other than null
	            result = charBuffer.subSequence(seen, length + seen).toString();
	            charBuffer.position(pos + length);
	        }
	        return result;
	    }
	    
	  }
}
