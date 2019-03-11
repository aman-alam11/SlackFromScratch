package edu.northeastern.ccs.im.authtest;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mock;

import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.database.JPAService;

import static org.junit.Assert.assertFalse;

public class TestBasicAuth {

  @Mock
  JPAService jpaService;

  @After
  public void logoutAll() {
    //SessionFactory.logoutAllUsers("admin");
  }


  @Test
  public void testLogin() {
    SessionFactory sessionFactory = SessionFactory.getInstance("alam",
            "123456", jpaService);
    assertFalse(sessionFactory.login());

    // Create an account

    // Now assert true for login
  }


  @Test
  public void testIllegalInputs() {

  }


  @Test
  public void testUsernameLengthLimit() {
    // TODO: Enforce limits after discussion
  }


  @Test
  public void testPasswordLengthLimit() {
    // TODO: Enforce limits after discussion
  }


  @Test
  public void testLogout() {
    SessionFactory sessionFactory = SessionFactory.getInstance("alam",
            "123456", jpaService);
    assertFalse(sessionFactory.login());

    // Create an account

    // Now assert true for login


    assertFalse(sessionFactory.logoutUser());
  }


}
