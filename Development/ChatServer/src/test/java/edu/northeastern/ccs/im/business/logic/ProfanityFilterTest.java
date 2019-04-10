package edu.northeastern.ccs.im.business.logic;

import org.junit.Assert;
import org.junit.Test;
import edu.northeastern.ccs.im.server.business.logic.handler.ProfanityFilter;


public class ProfanityFilterTest {

  @Test
  public void filterTest() {
    String testStr = "hey ass clown";
    Assert.assertEquals("hey *** clown ", ProfanityFilter.filterMessage(testStr));
  }

  @Test
  public void filterWordTest() {
    String testStr =  "fuck man";
    Assert.assertEquals("**** man ", ProfanityFilter.filterMessage(testStr));
  }
}
