package edu.northeastern.ccs.im.clientmenu.clientutils;

import org.junit.Before;
import org.junit.Test;

import edu.northeastern.ccs.im.message.MessageType;

import static org.junit.Assert.assertEquals;


public class GenericLoginCredentialTest {

  private GenerateLoginCredentials generateLoginCredentials;


  @Test
  public void genericTest() {
  	GenerateLoginCredentials.generateLoginCredentials(null, "user", MessageType.LOGIN);
    assertEquals("",GenerateLoginCredentials.getUsername());
  }
}
