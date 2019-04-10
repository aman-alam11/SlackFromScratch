package edu.northeastern.ccs.im.authtest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import edu.northeastern.ccs.im.auth.AuthModulesImpl;
import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AuthTest {

  private JPAService mJpaService;

  @Mock
  private JPAService mockedJPAService;

  private AuthModulesImpl authModules;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    mJpaService = JPAService.getInstance();
    authModules = new AuthModulesImpl();
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
//    for (User user : mJpaService.readAllUsers()) {
//      System.out.println(user.toString());
//    }

    // Now we try to login with correct password
    // assertTrue(SessionFactory.getInstance("cheetah", "pwd123", mJpaService).login());

    // Now we try to login with wrong password
    // assertFalse(SessionFactory.getInstance("cheetah", "pwd3", mJpaService).login());

    // Username does not exists
    // assertFalse(SessionFactory.getInstance("usernameDoesNotExists","pwd3", mJpaService).login());
  }


  @Test
  public void loginUserFailedTest() {

    when(mockedJPAService.findUserByName(eq("user"))).thenReturn(null);
    assertFalse(authModules.loginIn("user","pass", mockedJPAService));

  }

  @Test
  public void loginUserNewTest() {

//    when(mockedJPAService.findUserByName(eq("user"))).thenReturn(new User());
//    String hash = "$2a$10$8vfK8KcwZ9xw7I8Ek5OzkuwZp75cKxaEbc7m6lmzr.YVzDadBI162";
//    when(mockedJPAService.getHashFromUsername(eq("user"))).thenReturn(hash);
//    assertFalse(authModules.loginIn("user","pass",mockedJPAService));
  }

  //@Test
  public void createUserTest() {
    // Try to create a user with a username
    String username = "cheetahaknd";
//    assertTrue(SessionFactory.getInstance(username, "pwd123", mJpaService).createAccount());

    // Now create another account with same username
    assertFalse(SessionFactory.getInstance(username, "pwd123", mJpaService).createAccount());
  }
}
