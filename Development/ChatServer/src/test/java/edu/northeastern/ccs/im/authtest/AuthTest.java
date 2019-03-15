package edu.northeastern.ccs.im.authtest;

import org.junit.Before;
import org.junit.Test;

import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AuthTest {

  private JPAService mJpaService;

  @Before
  public void init() {
    mJpaService = new JPAService();
  }

  @Test
  public void testNulls() {
    // 000
    try {
      SessionFactory.getInstance(null, null, null).createAccount();
    } catch (Exception e) {
      assertEquals("Parameters cant be null", e.getMessage());
    }

    // 001
    try {
      SessionFactory.getInstance(null, null, mJpaService).createAccount();
    } catch (Exception e) {
      assertEquals("Parameters cant be null", e.getMessage());
    }

    // 010
    try {
      SessionFactory.getInstance(null, "pwd", null).createAccount();
    } catch (Exception e) {
      assertEquals("Parameters cant be null", e.getMessage());
    }

    // 011
    try {
      SessionFactory.getInstance(null, "pwd", mJpaService).createAccount();
    } catch (Exception e) {
      assertEquals("Parameters cant be null", e.getMessage());
    }

    // 100
    try {
      SessionFactory.getInstance("username", null, null).createAccount();
    } catch (Exception e) {
      assertEquals("Parameters cant be null", e.getMessage());
    }

    // 101
    try {
      SessionFactory.getInstance("username", null, mJpaService).createAccount();
    } catch (Exception e) {
      assertEquals("Parameters cant be null", e.getMessage());
    }

    // 110
    try {
      SessionFactory.getInstance("username", "pwd", null).createAccount();
    } catch (Exception e) {
      assertEquals("Parameters cant be null", e.getMessage());
    }
  }


  @Test
  public void loginUserTest() {
    for (User user : mJpaService.readAllUsers()) {
      System.out.println(user.toString());
    }

    // Now we try to login with correct password
    // assertTrue(SessionFactory.getInstance("cheetah", "pwd123", mJpaService).login());

    // Now we try to login with wrong password
    // assertFalse(SessionFactory.getInstance("cheetah", "pwd3", mJpaService).login());

    // Username does not exists
    // assertFalse(SessionFactory.getInstance("usernameDoesNotExists","pwd3", mJpaService).login());
  }


  @Test
  public void createUserTest() {
    // Try to create a user with a username
    String username = "cheetahaknd";
//    assertTrue(SessionFactory.getInstance(username, "pwd123", mJpaService).createAccount());

    // Now create another account with same username
    assertFalse(SessionFactory.getInstance(username, "pwd123", mJpaService).createAccount());
  }

}
