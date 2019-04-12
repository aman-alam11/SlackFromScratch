package edu.northeastern.ccs.im.followtest;

import org.junit.Test;

import edu.northeastern.ccs.im.model.UserFollow;

import static junit.framework.TestCase.assertEquals;


public class UserFollowTest {

  @Test
  public void test() {

    String userName = "myUser";
    String userToFollow = "follow";
    UserFollow userFollow = new UserFollow(userName, userToFollow);
    assertEquals(userToFollow, userFollow.getFollowerName());
    assertEquals(userName, userFollow.getUserName());

    userFollow.setFollowerName("newFollowerName");
    assertEquals(userFollow.getFollowerName(), "newFollowerName");

    userFollow.setUserName("newUserName");
    assertEquals(userFollow.getUserName(), "newUserName");
  }
}
