package edu.northeastern.ccs.im.auth;

public interface AuthModules {

  /**
   * Lets the user create an account.
   *
   * @param username The username chosen by user.
   * @param password The password chosen by user (At least 6 characters long).
   * @return A boolean representing if the create account operation was successful or not.
   */
  boolean createAccount(String username, String password);


  /**
   * Logs the user in.
   *
   * @param username The client's username.
   * @param password The client's password.
   * @return A boolean representing if the login was successful or not.
   */
  boolean loginIn(String username, String password);


  /**
   * Logs the user out.
   *
   * @return A boolean representing if the operation is successful or not.
   */
  boolean logout();


  /**
   * This can be through: (1) Query the server to check if the user is logged in or not and here we
   * need the client id. (2) Through a session manager where we wont need the client id (3) A way in
   * which we just query a variable (Not Recommended) and does not requires the client id as well.
   *
   * @param username The client's username.
   * @return A boolean representing if user is logged in or not.
   */
  boolean isLoggedIn(String username);


}
