package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.TranslateModel;
import edu.northeastern.ccs.im.server.Connection;
import edu.northeastern.ccs.im.server.business.logic.handler.TransLateHandler;

public class TransLateHandlerTest {

  private TransLateHandler transLateHandler;

  @Mock
  private Connection connection;

  @Mock
  SessionFactory sessionFactory;


  // need to initialize JPA service with session factory injection or it will call the actual
  // database. So we need to supress the warning.
  @SuppressWarnings("squid:S1854")
  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    transLateHandler = new TransLateHandler();
    JPAService jpaService = new JPAService(sessionFactory);
  }


  @Test
  public void transLateTest() {

    String user = "user";
    TranslateModel  translateModel = new TranslateModel("Str", "en");
    Assert.assertFalse(transLateHandler.handleMessage(user, new Gson().toJson(translateModel),connection));

  }

  @Test
  public void transLateFalseTest() {

    String user = "user";
    TranslateModel  translateModel = new TranslateModel("Str", "xx");


    Assert.assertFalse(transLateHandler.handleMessage(user, new Gson().toJson(translateModel),connection));

  }
}
