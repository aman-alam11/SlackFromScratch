package edu.northeastern.ccs.im;

import org.junit.Test;

import edu.northeastern.ccs.im.clientmenu.MainClass;

import static org.junit.Assert.assertEquals;

public class MainClassTest {

  @Test
  public void test() {
    try {
      MainClass.main(null);
    } catch (Exception e) {
      assertEquals("Model can't be null", e.getMessage());
    }
  }
}
