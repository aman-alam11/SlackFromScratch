package edu.northeastern.ccs.im.business.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import edu.northeastern.ccs.im.server.business.logic.ProfanityFilter;


public class ProfanityFilterTest {

  private ProfanityFilter profanityFilter;



  @Before
  public void init() {
    profanityFilter = ProfanityFilter.getInstace();
  }

  @Test
  public void filterTest() {
    String testStr = "hey ass clown";
    Assert.assertEquals("hey *** clown ", profanityFilter.filterMessage(testStr));
  }
}
