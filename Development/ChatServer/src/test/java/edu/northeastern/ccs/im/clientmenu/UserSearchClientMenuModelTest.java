package edu.northeastern.ccs.im.clientmenu;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.ccs.im.clientmenu.models.UserSearch;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;

public class UserSearchClientMenuModelTest {

  @Test
  public void test() {
    UserSearch userSearch = new UserSearch("userName");
    assertEquals("userName", userSearch.getUsername());
    assertNull(userSearch.getListUserString());
    List<String> dummyUserList = new ArrayList<>(7);
    for (int i = 0; i < dummyUserList.size(); i++) {
      dummyUserList.add("NEWUSER" + i);
    }

    userSearch.setUsersList(dummyUserList);
    assertEquals(dummyUserList, userSearch.getListUserString());
  }
}
