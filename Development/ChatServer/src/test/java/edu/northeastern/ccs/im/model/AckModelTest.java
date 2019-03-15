package edu.northeastern.ccs.im.model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
  }


  @Test
  public void testOverloadedConstructor(){
    AckModel mAckModel = new AckModel(false, "", false, "newUser");
    assertFalse(mAckModel.isUserAuthenticated());
    assertFalse(mAckModel.isLogin());
    assertEquals("", mAckModel.getErrorMessage());
    assertEquals("newUser", mAckModel.getUsername());
  }

}