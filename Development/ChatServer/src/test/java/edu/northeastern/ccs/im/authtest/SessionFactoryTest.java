package edu.northeastern.ccs.im.authtest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.assertFalse;

import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class SessionFactoryTest {

  @Mock
  JPAService jpaService;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void loginTestSessionFactory() {

    when(jpaService.findUserByName(eq("username"))).thenReturn(new User());
    String hash = "$2a$10$rDv1bmwHaAmxWl7S9lKqMeL5trsapPONJriMZpnWRnrR19utgVND.";
    when(jpaService.getHashFromUsername("user")).thenReturn(hash);

    SessionFactory sessionFactory = SessionFactory
            .getInstance("user","pass",jpaService);
    assertFalse(sessionFactory.login());
  }
}
