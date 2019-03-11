package edu.northeastern.ccs.im.auth;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import edu.northeastern.ccs.im.database.JPAService;

/**
 * A class which mimics the session as we are not required to implement session. We perform Auth
 * related operations here.
 */
public final class SessionFactory {

  private static SessionFactory mLoginInstance;

  // Inject Database layer for easy testing and separation of concern
  private JPAService mJpaService;


  // Use the specific version of AuthModulesImpl and not superclass as the interface can be
  // implemented by any class
  private AuthModulesImpl mAuthModules;

  private String mUsername;
  private String mPassword;

  private SessionFactory(String rawUsername, String rawPassword, JPAService jpaService) {
    // Private Constructor
    this.mJpaService = jpaService;
    this.sanitizeInput(rawUsername, rawPassword);
    mAuthModules = new AuthModulesImpl();
  }


  /**
   * A gateway class to perform auth related operations like login/create_account etc.
   *
   * @return An instance of session factory (which actually mimics the session as we are not
   * required to implement session). Since we will do auth related operations through this class,
   * this is referred to as session factory.
   */
  public static SessionFactory getInstance(String rawUsername, String rawPassword, JPAService jpaService) {
    if (rawUsername == null || rawPassword == null || jpaService == null) {
      throw new IllegalArgumentException("Parameters cant be null");
    }

    if (mLoginInstance == null) {
      mLoginInstance = new SessionFactory(rawUsername, rawPassword, jpaService);
    }
    return mLoginInstance;
  }


  /**
   * Sanitize the username and password before performing any database operation on them.
   *
   * @param rawUsername The raw String entered by user as username.
   * @param rawPassword The raw String entered by user as password.
   */
  private void sanitizeInput(String rawUsername, String rawPassword) {
    this.mUsername = Jsoup.clean(rawUsername, Whitelist.basic());
    this.mPassword = Jsoup.clean(rawPassword, Whitelist.basic());
  }


  public boolean login() {
    boolean loginSuccessful = mAuthModules.loginIn(mUsername, mPassword, mJpaService);
    removePreviousRef();
    return loginSuccessful;
  }

  public boolean createAccount() {
    boolean createAccountSuccess = mAuthModules.createAccount(mUsername, mPassword, mJpaService);
    removePreviousRef();
    return createAccountSuccess;
  }

  private void removePreviousRef() {
    mUsername = null;
    mPassword = null;
  }
}
