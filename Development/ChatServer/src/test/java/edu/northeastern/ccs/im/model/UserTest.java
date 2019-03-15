package edu.northeastern.ccs.im.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserTest {

  @Test
  public void test() {
    User user = new User("user", "pwd", "email123@email.com");
    assertEquals("email123@email.com", user.getEmail());
    assertNull(user.getFirstName());
    assertNull(user.getLastName());
    assertEquals("pwd", user.getPassword());
    assertEquals("user", user.getUserName());
  }

  @Test
  public void testSetMethods(){
    User user = new User("user", "pwd", "email123@email.com");
    assertEquals("email123@email.com", user.getEmail());
    assertNull(user.getFirstName());
    assertNull(user.getLastName());
    assertEquals("pwd", user.getPassword());
    assertEquals("user", user.getUserName());

    user.setEmail("newemail@email.com");
    assertEquals("newemail@email.com", user.getEmail());

    user.setFirstName("firstName");
    assertEquals("firstName", user.getFirstName());

    user.setLastName("lastName");
    assertEquals("lastName", user.getLastName());

    user.setUserName("newusername");
    assertEquals("newusername", user.getUserName());

    user.setPassword("pwd123");
    assertEquals("pwd123", user.getPassword());
  }

}
