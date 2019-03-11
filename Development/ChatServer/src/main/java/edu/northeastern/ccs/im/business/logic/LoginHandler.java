package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;

import java.util.logging.Logger;

import edu.northeastern.ccs.im.auth.SessionFactory;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.model.LoginCredentials;

public class LoginHandler implements MessageHandler {

  private Gson gson;

  public LoginHandler() {
    gson = new Gson();
  }

  @Override
  public boolean handleMessage(String user, String message) {
    try {
      LoginCredentials lgn = gson.fromJson(message, LoginCredentials.class);

      // TODO: Use the overloaded method in interface and use the injected jpaservice here
      SessionFactory sessionFactory = SessionFactory.getInstance(lgn.getUserName(), lgn.getPassword(),
              new JPAService());

      return sessionFactory.login();
    } catch (Exception e) {
      Logger.getLogger(this.getClass().getSimpleName()).info(e.getMessage());
      return false;
    }
  }

}
