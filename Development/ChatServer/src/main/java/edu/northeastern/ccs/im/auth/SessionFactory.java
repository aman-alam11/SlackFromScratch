package edu.northeastern.ccs.im.auth;

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
    // TODO: Sanitize username and password and then assign
    this.mUsername = rawUsername;
    this.mPassword = rawPassword;
  }


  /**
   * Encrypt the password using salting and Hashing (Used internally by BCrypt).
   *
   * @param rawPassword The sanitized raw password String.
   */
  private void encryptPassword(String rawPassword) {
    // TODO
  }


  public boolean login() {
    if (mAuthModules.isLoggedIn(mUsername)) {
      return true;
    }

    return mAuthModules.loginIn(mUsername, mPassword);
  }

  public boolean createAccount() {
    return mAuthModules.createAccount(mUsername, mPassword);
  }


  public boolean logoutUser() {
    return mAuthModules.logout();
  }


}
