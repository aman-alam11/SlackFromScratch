package edu.northeastern.ccs.im.model;

import edu.northeastern.ccs.im.clientmenu.clientutils.GenerateLoginCredentials;
import org.junit.Test;

import static org.junit.Assert.*;

public class AckModelTest {


  @Test
  public void testTT() {
    AckModel mAckModel = new AckModel(true, "", true);
    assertTrue(mAckModel.isUserAuthenticated());
		assertTrue(mAckModel.isLogin());
		assertEquals("", mAckModel.getErrorMessage());
  }


  @Test
  public void testTF() {
    AckModel mAckModel = new AckModel(true, "", false);
    assertTrue(mAckModel.isUserAuthenticated());
    assertFalse(mAckModel.isLogin());
    assertEquals("", mAckModel.getErrorMessage());
  }



  @Test
  public void testFT() {
    AckModel mAckModel = new AckModel(false, "", true);
    assertFalse(mAckModel.isUserAuthenticated());
    assertTrue(mAckModel.isLogin());
    assertEquals("", mAckModel.getErrorMessage());
  }


  @Test
  public void testFF() {
    AckModel mAckModel = new AckModel(false, "", false);
    assertFalse(mAckModel.isUserAuthenticated());
    assertFalse(mAckModel.isLogin());
    assertEquals("", mAckModel.getErrorMessage());
    new GenerateLoginCredentials();
  }

}