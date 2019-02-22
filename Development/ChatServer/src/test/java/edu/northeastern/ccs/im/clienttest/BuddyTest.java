package clienttest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import edu.northeastern.ccs.im.client.Buddy;

import static org.junit.Assert.assertEquals;

public class BuddyTest {

  private Buddy mBuddy;
  private static final String TEST_BUDDY_NAME = "TEST BUDDY NAME";

  // Private Methods
  private static final String GET_BUDDY_SINGLETON_METHOD_NAME = "getBuddy";
  private static final String REMOVE_BUDDY_METHOD_NAME = "removeBuddy";
  private static final String EMPTY_BUDDY_METHOD_NAME = "getEmptyBuddy";

  // Private Field
  private static final String CONCURRENT_MAP_NAME = "INSTANCES";

  // Logger for class
  private static final Logger BUDDY_TEST_LOGGER = Logger.getLogger(BuddyTest.class.getSimpleName());


  @Before
  public void init() {
    // Initialize All Tests params
    mBuddy = Buddy.makeTestBuddy(TEST_BUDDY_NAME);
  }

  @After
  public void deInit(){
    // Now Remove The Buddy and clear the concurrent Map as it is in Static Initializer Block
    try {
      Method getRemoveBuddyMethod = Buddy.class.getDeclaredMethod(REMOVE_BUDDY_METHOD_NAME,
              String.class);
      getRemoveBuddyMethod.setAccessible(true);
      getRemoveBuddyMethod.invoke(mBuddy, TEST_BUDDY_NAME);

      Field concurrentMap = Buddy.class.getDeclaredField(CONCURRENT_MAP_NAME);
      concurrentMap.setAccessible(true);

      // Clear the Map
      ((ConcurrentHashMap<String, Buddy>) concurrentMap.get(mBuddy)).clear();
    } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException
            | InvocationTargetException e) {
      BUDDY_TEST_LOGGER.info(e.getMessage());
    }
  }

  @Test
  public void testNullInput() {
    mBuddy = Buddy.makeTestBuddy(null);
    assertEquals(mBuddy.getUserName(), "");
  }

  @Test
  public void testBuddyUsingInstance() {
    assertEquals(mBuddy.getUserName(), TEST_BUDDY_NAME);
  }

  @Test
  public void testBuddy() {

    // Get Buddy First
    try {
      Method getBuddyMethod = Buddy.class.getDeclaredMethod(GET_BUDDY_SINGLETON_METHOD_NAME,
              String.class);
      getBuddyMethod.setAccessible(true);
      getBuddyMethod.invoke(mBuddy, TEST_BUDDY_NAME);
      assertEquals(TEST_BUDDY_NAME, mBuddy.getUserName());
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      BUDDY_TEST_LOGGER.info(e.getMessage());
    }


    // Now Remove The Buddy
    try {
      Method getRemoveBuddyMethod = Buddy.class.getDeclaredMethod(REMOVE_BUDDY_METHOD_NAME,
              String.class);
      getRemoveBuddyMethod.setAccessible(true);
      getRemoveBuddyMethod.invoke(mBuddy, TEST_BUDDY_NAME);

      Field concurrentMap = Buddy.class.getDeclaredField(CONCURRENT_MAP_NAME);
      concurrentMap.setAccessible(true);
      Map<String, Buddy> mainMap = (ConcurrentHashMap<String, Buddy>) concurrentMap.get(mBuddy);
      assertEquals(0, mainMap.size());
    } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException
            | InvocationTargetException e) {
      BUDDY_TEST_LOGGER.info(e.getMessage());
    }
  }


  @Test
  public void testGetEmptyBuddy() {
    try {
      Method getEmptyBuddyMethod = Buddy.class.getDeclaredMethod(EMPTY_BUDDY_METHOD_NAME,
              String.class);
      getEmptyBuddyMethod.setAccessible(true);
      getEmptyBuddyMethod.invoke(mBuddy, TEST_BUDDY_NAME);

      // Check if the Concurrent Map has the new String or not and hence the size will be 0
      // Correct output : Should NOT have the String and size should be 0
      Field concurrentMap = Buddy.class.getDeclaredField(CONCURRENT_MAP_NAME);
      concurrentMap.setAccessible(true);
      Map<String, Buddy> mainMap = (ConcurrentHashMap<String, Buddy>) concurrentMap.get(mBuddy);
      assertEquals(mainMap.size(), 0);
    } catch (NoSuchMethodException | IllegalAccessException
            | InvocationTargetException | NoSuchFieldException e) {
      BUDDY_TEST_LOGGER.info(e.getMessage());
    }
  }


  @Test
  public void addMoreTestBranchCoverage(){
    try {
      Method getBuddyMethod = Buddy.class.getDeclaredMethod(GET_BUDDY_SINGLETON_METHOD_NAME,
              String.class);
      getBuddyMethod.setAccessible(true);
      getBuddyMethod.invoke(mBuddy, TEST_BUDDY_NAME);

      // Now the String TEST_BUDDY_NAME is already in Map
      Method getEmptyBuddyMethod = Buddy.class.getDeclaredMethod(EMPTY_BUDDY_METHOD_NAME,
              String.class);
      getEmptyBuddyMethod.setAccessible(true);
      getEmptyBuddyMethod.invoke(mBuddy, TEST_BUDDY_NAME);

      // Call it the second time to check alternate branch when the name is already there
      getEmptyBuddyMethod = Buddy.class.getDeclaredMethod(GET_BUDDY_SINGLETON_METHOD_NAME,
              String.class);
      getEmptyBuddyMethod.setAccessible(true);
      getEmptyBuddyMethod.invoke(mBuddy, TEST_BUDDY_NAME);
       assertEquals(TEST_BUDDY_NAME, mBuddy.getUserName());

    } catch (NoSuchMethodException | IllegalAccessException
            | InvocationTargetException e) {
      BUDDY_TEST_LOGGER.info(e.getMessage());
    }
  }
}
