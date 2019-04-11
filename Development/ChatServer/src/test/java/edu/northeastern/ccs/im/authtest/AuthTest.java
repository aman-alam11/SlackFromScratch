package edu.northeastern.ccs.im.authtest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
  Session session;

  @Mock
  Transaction transaction;

  @Mock
  org.hibernate.SessionFactory sessionFactoryMock;

  @Mock
  Query query;

  @Mock
  NativeQuery nativeQuery;


  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    mJpaService = new JPAService(sessionFactoryMock);
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
  public void loginUserFailedTest() {

    User user = new User();
    String hash = "$2a$10$KjdkJgd.RKn4jgiViFzOne1xhRLcFkLQ371rViEYpjC6rxq0HMXa.";

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //From user
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(query.getSingleResult()).thenReturn(null);

    assertFalse(SessionFactory.getInstance("username", "pawd", mJpaService).login());
  }



}
