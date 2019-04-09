package edu.northeastern.ccs.im.client.communication;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.message.MessageJson;

public class SocketConnection implements Connection {

  private static final String TAG = SocketConnection.class.getSimpleName();
  private static Queue<MessageJson> messageQueue;
  private static Connection instance;
  private static SocketChannel channel;
  private static final int BUFFER_SIZE = 64 * 1024;
  private static final String CHARSET_NAME = "us-ascii";
  private static final String MESSAGE_START_KEY = "#{";
  private static final String MESSAGE_END_KEY = "}#";
  private static final String MESSAGE_SEPARATOR = "#";
  private static Gson gson;

  private static StringBuilder messageBuffer;
  private static boolean isAlive;

  //singleton implementation
  private SocketConnection() {
  	
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
        ChatLogger.error(TAG + " : " + e.getMessage());
        return null;
      }
    }
    return instance;
  }

  @Override
  public void terminate() {
  	synchronized (SocketConnection.class) {
  		isAlive = false;
		}
  }

  private static void initThread() {
	  messageQueue = new ConcurrentLinkedQueue<>();
	  messageBuffer = new StringBuilder();
	  Thread messageListenerFromServer = new Thread(() -> {
       isAlive = true;
      ByteBuffer buff = ByteBuffer.allocate(BUFFER_SIZE);
      while (isAlive) {
        try {
          int dataRead = channel.read(buff);
          if (dataRead > 0) {
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
          ChatLogger.error(e.getMessage());
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
      ChatLogger.info(TAG + "Message in buffer :" + msgBufferString);
      if (msgBufferString.contains(MESSAGE_START_KEY) && msgBufferString.contains(MESSAGE_END_KEY)) {
        int start = msgBufferString.indexOf(MESSAGE_START_KEY) + 1;
        int end = msgBufferString.indexOf(MESSAGE_END_KEY) + 1;
        String msg = msgBufferString.substring(start, end);
        ChatLogger.info(TAG + "Message extracted from buffer :" + msg);
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
  		ChatLogger.info(TAG + "Number of messages in queue :" + messageQueue.size());
      MessageJson msg = messageQueue.poll();
      ChatLogger.info(TAG + "Next Message :" + msg);
      return msg;
  }

  @Override
  public boolean sendMessage(MessageJson message) {
    String str = MESSAGE_SEPARATOR + gson.toJson(message) + MESSAGE_SEPARATOR;
    ByteBuffer b = ByteBuffer.wrap(str.getBytes());
    while (b.hasRemaining()) {
      try {
        channel.write(b);
      } catch (IOException e) {
        ChatLogger.error(e.getMessage());
        return false;
      }
    }
    b.clear();
    return true;
  }
}
