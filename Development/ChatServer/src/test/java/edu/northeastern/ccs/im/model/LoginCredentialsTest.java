package edu.northeastern.ccs.im.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class LoginCredentialsTest {

  @Test
  public void testEmptyConstructor() {
    LoginCredentials credentialsEmptyConstructor = new LoginCredentials();
    assertNull(credentialsEmptyConstructor.getPassword());
    assertNull(credentialsEmptyConstructor.getUserName());
    credentialsEmptyConstructor.setPassword("newpwd");
    credentialsEmptyConstructor.setUserName("newuser");
    assertEquals("newpwd", credentialsEmptyConstructor.getPassword());
    assertEquals("newuser", credentialsEmptyConstructor.getUserName());
  }


  @Test
  public void testParamsConstructor() {
    LoginCredentials credentialsParamsConstructor = new LoginCredentials("myUser",
            "pwd123");
    assertEquals("myUser", credentialsParamsConstructor.getUserName());
    assertEquals("pwd123", credentialsParamsConstructor.getPassword());
    credentialsParamsConstructor.setUserName("newuser1");
    credentialsParamsConstructor.setPassword("newpwd1");
    assertEquals("newuser1", credentialsParamsConstructor.getUserName());
    assertEquals("newpwd1", credentialsParamsConstructor.getPassword());
  }

}
