package edu.northeastern.ccs.im.clientmenu.clientutils;

import org.junit.Before;
import org.junit.Test;

import edu.northeastern.ccs.im.message.MessageType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GenericLoginCredentialTest {

  private GenerateLoginCredentials generateLoginCredentials;

  @Before
  public void init() {
    generateLoginCredentials =  new GenerateLoginCredentials();
  }

  @Test
  public void genericTest() {
    generateLoginCredentials.generateLoginCredentials(null, "user", MessageType.LOGIN);
    assertEquals("",GenerateLoginCredentials.getUsername());
  }

}
