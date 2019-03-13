package edu.northeastern.ccs.im.auth;

import edu.northeastern.ccs.im.database.JPAService;

public interface AuthModules {

  /**
   * Lets the user create an account.
   *
   * @param username    The username chosen by user.
   * @param password    The password chosen by user (At least 6 characters long).
   * @param mJpaService The injected database layer.
   * @return A boolean representing if the create account operation was successful or not.
   */
  boolean createAccount(String username, String password, JPAService mJpaService);


  /**
   * Logs the user in.
   *
   * @param username    The client's username.
   * @param password    The client's password.
   * @param mJpaService The injected database layer.
   * @return A boolean representing if the login was successful or not.
   */
  boolean loginIn(String username, String password, JPAService mJpaService);

}
