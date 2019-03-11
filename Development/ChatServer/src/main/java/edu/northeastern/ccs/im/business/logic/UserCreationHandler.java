package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.LoginCredentials;

public class UserCreationHandler implements MessageHandler {

  private Gson gson;

  public UserCreationHandler() {
    gson = new Gson();
  }


  @Override
  public boolean handleMessage(String user, String message) {

    LoginCredentials loginCredentials = gson.fromJson(message, LoginCredentials.class);

    // TODO: Use the overloaded method in interface and use the injected jpaservice here
    SessionFactory sessionFactory = SessionFactory.getInstance(loginCredentials.getUserName(),
            loginCredentials.getPassword(), new JPAService());

    return sessionFactory.createAccount();
  }

}
