package edu.northeastern.ccs.im.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UserSearchTest {

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
