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

  private static String mUsername;
  private static String mPassword;

  private SessionFactory(JPAService jpaService) {
    // Private Constructor
    this.mJpaService = jpaService;
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
      mLoginInstance = new SessionFactory(jpaService);
    }
    sanitizeInput(rawUsername, rawPassword);

    return mLoginInstance;
  }


  /**
   * Sanitize the username and password before performing any database operation on them.
   *
   * @param rawUsername The raw String entered by user as username.
   * @param rawPassword The raw String entered by user as password.
   */
  private static void sanitizeInput(String rawUsername, String rawPassword) {
    mUsername = Jsoup.clean(rawUsername, Whitelist.basic());
    mPassword = Jsoup.clean(rawPassword, Whitelist.basic());
  }


  /**
   * Used to login user.
   * @return boolean - true/false depending on success.
   */
  public boolean login() {
    boolean loginSuccessful = mAuthModules.loginIn(mUsername, mPassword, mJpaService);
    removePreviousRef();
    return loginSuccessful;
  }

  /**
   * To create a new account.
   * @return boolean - true/false depending on success.
   */
  public boolean createAccount() {
    boolean createAccountSuccess = mAuthModules.createAccount(mUsername, mPassword, mJpaService);
    removePreviousRef();
    return createAccountSuccess;
  }

  private static void removePreviousRef() {
    mUsername = null;
    mPassword = null;
  }
}
