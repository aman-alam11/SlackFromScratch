package clienttest;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.client.KeyboardScanner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


public class KeyboardScannerTest {

  private KeyboardScanner mKeyboardScanner;
  private Thread mProducerThread;
  private static final Logger KEYBOARD_TEST_LOGGER = Logger
          .getLogger(KeyboardScannerTest.class.getSimpleName());

  @Before
  public void initSingleton() {
    try {
      Method singletonMethod = KeyboardScanner.class.getDeclaredMethod("getInstance");
      singletonMethod.setAccessible(true);
      mKeyboardScanner = (KeyboardScanner) singletonMethod.invoke(null, null);

      Field producerField = KeyboardScanner.class.getDeclaredField("producer");
      producerField.setAccessible(true);
      mProducerThread = (Thread) producerField.get(singletonMethod);
      assertEquals("Key scanning thread", mProducerThread.getName());

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException
            | NoSuchFieldException e) {
      KEYBOARD_TEST_LOGGER.info(e.getMessage());
    }
  }

  @Test
  public void testRestartNotNullNotTerminated() {
    assertFalse(mKeyboardScanner.hasNext());
    // Do nothing to producer as Before blocks leads to not null and not terminated
    assertEquals("RUNNABLE", mProducerThread.getState().toString());

    try {
      Method restartMethod = KeyboardScanner.class.getDeclaredMethod("restart");
      restartMethod.setAccessible(true);
      restartMethod.invoke(mKeyboardScanner);
      assertEquals("RUNNABLE", mProducerThread.getState().toString());


    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      KEYBOARD_TEST_LOGGER.info(e.getMessage());
    }
  }


  @Test
  public void testRestartNullNotTerminated() {
    mProducerThread = null;

    // Call to restart when the thread is not terminated and not null
    try {
      Field producerField = KeyboardScanner.class.getDeclaredField("producer");
      producerField.setAccessible(true);
      mProducerThread = (Thread) producerField.get(mKeyboardScanner);

      Method restartMethod = KeyboardScanner.class.getDeclaredMethod("restart");
      restartMethod.setAccessible(true);
      restartMethod.invoke(mKeyboardScanner);

      assertEquals("RUNNABLE", mProducerThread.getState().toString());

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
      KEYBOARD_TEST_LOGGER.info(e.getMessage());
    }
  }

  @Test
  public void testRestartNotNullTerminated() {
    mProducerThread.interrupt();

    // Call to restart when the thread is not terminated and not null
    try {
      Method restartMethod = KeyboardScanner.class.getDeclaredMethod("restart");
      restartMethod.setAccessible(true);
      restartMethod.invoke(mKeyboardScanner);
      assertEquals("RUNNABLE", mProducerThread.getState().toString());

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      KEYBOARD_TEST_LOGGER.info(e.getMessage());
    }
  }

  @Test
  public void testRestartNullTerminated() {
    mProducerThread.interrupt();
    mProducerThread = null;

    // Call to restart when the thread is not terminated and not null
    try {
      // Attach the ref to current Field to the field in object
      Field producerField = KeyboardScanner.class.getDeclaredField("producer");
      producerField.setAccessible(true);
      mProducerThread = (Thread) producerField.get(mKeyboardScanner);

      Method restartMethod = KeyboardScanner.class.getDeclaredMethod("restart");
      restartMethod.setAccessible(true);
      restartMethod.invoke(mKeyboardScanner);

    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
      KEYBOARD_TEST_LOGGER.info(e.getMessage());
    }
  }


}
