package edu.northeastern.ccs.im.auth;

public class AuthModulesImpl implements AuthModules {


  public AuthModulesImpl() {

  }

  @Override
  public boolean createAccount(String username, String password) {
    return false;
  }

  @Override
  public boolean loginIn(String username, String password) {
    return false;
  }

  @Override
  public boolean logout() {
    return false;
  }

  @Override
  public boolean isLoggedIn() {
    return false;
  }
}
