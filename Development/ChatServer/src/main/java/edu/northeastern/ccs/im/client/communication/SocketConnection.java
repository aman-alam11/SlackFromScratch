package edu.northeastern.ccs.im.client.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.message.MessageJson;
import edu.northeastern.ccs.im.message.MessageType;

public class SocketConnection implements Connection {

	private static Queue<MessageJson> messageQueue;
	private static Connection instance; 
	private static SocketChannel channel;
	private static final int BUFFER_SIZE = 64 * 1024;
	private static final String CHARSET_NAME = "us-ascii";
	private static final String MESSAGE_START_KEY = "#{";
	private static final String MESSAGE_END_KEY = "}#";
	private static final String MESSAGE_SEPARATOR ="#";
	private static Gson gson;

	private static StringBuilder messageBuffer;
	
	//singleton implementation
	private  SocketConnection() {
	}
	
	public static synchronized Connection getInstance(String url, int port) {
		if (instance == null) {
			try {
				instance = new SocketConnection();
				channel = SocketChannel.open();
				channel.configureBlocking(false);
				channel.connect(new InetSocketAddress(url, port));
				gson = new Gson();
				if (channel.finishConnect()) {
					initThread();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return instance;
	}
	
	private static void initThread() {
		messageQueue = new ConcurrentLinkedQueue<>();
		messageBuffer = new StringBuilder();
		Thread messageListenerFromServer = new Thread(() -> {
			boolean isAlive = true;
			ByteBuffer buff = ByteBuffer.allocate(BUFFER_SIZE);
			while (isAlive) {
				try {
					int dataRead = channel.read(buff);
					// Check if client in dead
					if (dataRead < 0) {
						messageQueue.add(new MessageJson(MessageType.LOG_OUT));
						isAlive = false;
					} else if (dataRead > 0){
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
					decodeMessage();
				} catch (IOException e) {
					e.printStackTrace();
					isAlive = false;
				}
			}
		});
		messageListenerFromServer.start();
	}
	
	private static boolean decodeMessage() {
		boolean isMessageDecoded = false;
		if (messageBuffer.length() > 0) {
			String msgBufferString = messageBuffer.toString();
			if (msgBufferString.contains(MESSAGE_START_KEY) && msgBufferString.contains(MESSAGE_END_KEY)) {
				int start = msgBufferString.indexOf(MESSAGE_START_KEY) + 1;
				int end = msgBufferString.indexOf(MESSAGE_END_KEY) + 1;
				String msg = msgBufferString.substring(start, end);
				try {
					// Extract message
					MessageJson extractedMessage = gson.fromJson(msg, MessageJson.class);
					isMessageDecoded = true;
					messageQueue.add(extractedMessage);
					messageBuffer.delete(start - 1, end + 1);
				} catch (JsonSyntaxException e) {
					ChatLogger.error(e.getMessage());
					messageBuffer.delete(start - 1, end + 1);
				}
			}
		}
		return isMessageDecoded;
	}

	@Override
	public boolean isConnected() {
		return channel.isConnected();
	}

	@Override
	public boolean hasNext() {
		return !messageQueue.isEmpty();
	}

	@Override
	public MessageJson next() {
		if (hasNext()) {
			return messageQueue.remove();
		}
		return null;
	}

	@Override
	public boolean sendMessage(MessageJson message) {
		String str = MESSAGE_SEPARATOR + gson.toJson(message) + MESSAGE_SEPARATOR;
		ByteBuffer b = ByteBuffer.wrap(str.getBytes());
		while(b.hasRemaining()) {
			try {
				channel.write(b);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		b.clear();
		return true;
	}
}
