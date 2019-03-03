package edu.northeastern.ccs.im.auth;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class AuthModulesImpl implements AuthModules {

  @Override
  public boolean createAccount(String username, String password) {
    // Save the username and hash
    try {
      BCrypt.hashpw(password, BCrypt.gensalt());
      // TODO: Save in DATABASE with username
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean loginIn(String username, String password) {

    // Get the hash for the username from user and verify using
//    String hash = DatabaseLayer.getHashForUsername(username);
//    return BCrypt.checkpw(password, hash);
    return false;
  }

  @Override
  public boolean logout() {
    return false;
  }

  @Override
  public boolean isLoggedIn(String username) {
    return false;
  }

  @Override
  public void logoutAllUsers() {

  }

  @Override
  public boolean isSuperUser() {
    return false;
  }
}
