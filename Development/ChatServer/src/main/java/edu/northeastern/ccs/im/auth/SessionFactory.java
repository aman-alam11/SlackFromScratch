package edu.northeastern.ccs.im.auth;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 * A class which mimics the session as we are not required to implement session. We perform Auth
 * related operations here.
 */
public final class SessionFactory {

  private static SessionFactory mLoginInstance;

  // Use the specific version of AuthModulesImpl and not superclass as the interface can be
  // implemented by any class
  private AuthModulesImpl mAuthModules;

  private String mUsername;
  private String mPassword;

  private SessionFactory(String rawUsername, String rawPassword) {
    // Private Constructor
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
  public static SessionFactory getInstance(String rawUsername, String rawPassword) {
    if (mLoginInstance == null) {
      mLoginInstance = new SessionFactory(rawUsername, rawPassword);
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
    if (mAuthModules.isLoggedIn(mUsername)) {
      return true;
    }

    boolean loginSuccessful = mAuthModules.loginIn(mUsername, mPassword);
    this.mPassword = null;
    return loginSuccessful;
  }

  public boolean createAccount() {
    boolean createAccountSuccess = mAuthModules.createAccount(mUsername, mPassword);
    this.mPassword = null;
    return createAccountSuccess;
  }


  public boolean logoutUser() {
    return mAuthModules.logout();
  }


  public static void logoutAllUsers(String mUsername) {
    // If current user has admin privileges, logout all users
    AuthModules authModule = new AuthModulesImpl();
    if (authModule.isLoggedIn(mUsername) && authModule.isSuperUser()) {
      authModule.logoutAllUsers();
    } else {
      throw new UnsupportedOperationException("You do not have admin rights to perform this operation");
    }
  }


}
