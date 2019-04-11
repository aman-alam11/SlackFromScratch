package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigInteger;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import edu.northeastern.ccs.im.database.Group;
import edu.northeastern.ccs.im.database.GroupMember;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.UserFollow;
import edu.northeastern.ccs.im.model.RecallModel;
import edu.northeastern.ccs.im.model.TranslateModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.RecallUserChatHandler;
import edu.northeastern.ccs.im.server.business.logic.handler.TransLateHandler;

import static org.mockito.Mockito.when;

public class RecallChatHandlerTest {


  private RecallUserChatHandler recallUserChatHandler;


  @Mock
  SessionFactory sessionFactoryMock;

  @Mock
  Session session;


  @Mock
  Query query;

  @Mock
  NativeQuery nativeQuery;

  @Mock
  Transaction transaction;


  @Mock
  private Connection connection;


  // need to initialize JPA service with session factory injection or it will call the actual
  // database. So we need to supress the warning.
  @SuppressWarnings("squid:S1854")
  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    recallUserChatHandler = new RecallUserChatHandler();

    JPAService jpaService = new JPAService(sessionFactoryMock);
  }


  @Test
  public void transLateTest() {

    String user = "user";


    BigInteger userId = BigInteger.ONE;

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Get user from UserID
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(userId);
    when(query.executeUpdate()).thenReturn(1);


    RecallModel translateModel = new RecallModel("ste", 1);
    Assert.assertFalse(recallUserChatHandler.handleMessage(user, new Gson().toJson(translateModel), connection));

  }
}
