package edu.northeastern.ccs.im.clienttest;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import edu.northeastern.ccs.im.client.IMConnection;
import edu.northeastern.ccs.im.client.Message;
import edu.northeastern.ccs.im.client.ScanForMessagesWorker;
import edu.northeastern.ccs.im.client.SocketNB;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageWorkerTest {

  private ByteArrayOutputStream outputStream;

  @Before
  public void init() {
    outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
  }

  @Test
  public void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
    Constructor constructor = ScanForMessagesWorker.class.getDeclaredConstructor(IMConnection.class, SocketNB.class);
    constructor.setAccessible(true);
    ScanForMessagesWorker scanForMessagesWorker =
            (ScanForMessagesWorker) constructor.newInstance(
                    new IMConnection("localhost", 4545, "johndoe"),
                    new SocketNB("localhost", 4545));

    Method processMethod = ScanForMessagesWorker.class.getDeclaredMethod("process", List.class);
    processMethod.setAccessible(true);
    processMethod.invoke(scanForMessagesWorker, new ArrayList<>());

    Field messageField = ScanForMessagesWorker.class.getDeclaredField("messages");
    messageField.setAccessible(true);
    List<Message> messageList = (List<Message>) messageField.get(scanForMessagesWorker);
    assertEquals(0, messageList.size());

    Method doInBackground = ScanForMessagesWorker.class.getDeclaredMethod("doInBackground");
    doInBackground.setAccessible(true);
    try {
      doInBackground.invoke(scanForMessagesWorker);
    } catch (Exception e) {
      assertNull(e.getMessage());
      System.setOut(System.out);
    }
  }
}
