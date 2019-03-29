package edu.northeastern.ccs.im.clientmenu;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.ccs.im.clientmenu.models.Search;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class SearchClientMenuModelTest {

  @Test
  public void test() {
    Search search = new Search("userName");
    assertEquals("userName", search.getUsername());
    assertNull(search.getListString());
    List<String> dummyUserList = new ArrayList<>(7);
    for (int i = 0; i < dummyUserList.size(); i++) {
      dummyUserList.add("NEWUSER" + i);
    }

    search.setList(dummyUserList);
    assertEquals(dummyUserList, search.getListString());
  }
}
