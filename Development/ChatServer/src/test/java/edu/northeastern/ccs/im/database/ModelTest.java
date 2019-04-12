package edu.northeastern.ccs.im.database;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class ModelTest {

  @Test
  public void chatTest() {

    User user = new User();
    user.setId(0);
    user.setName("atti");

    Group group = new Group();
    group.setId(2);

    Chat chat = new Chat();
    chat.setId(1);
    chat.setToId(user);
    chat.setGroupId(group);
    chat.setExpiry(new Date());
    chat.setIsDelivered(true);

    Assert.assertEquals(1, chat.getId());
    Assert.assertEquals(user.getName(), chat.getToId().getName());
    Assert.assertEquals(group.getId(), chat.getGroupId().getId());
    Assert.assertEquals( new Date().toString(), chat.getExpiry().toString());
    Assert.assertTrue(chat.getIsDelivered());

  }


  @Test
  public void groupTest() {

    User user = new User();
    user.setName("atti");

    Group  group = new Group();
    group.setgName("atti");
    group.setgCreator(user);
    group.setCreationDate(new Date());
    group.setModeratorAuthRequired(true);
    Assert.assertEquals("atti", group.getgName());
    Assert.assertEquals("atti", group.getgCreator().getName());
    Assert.assertEquals( new Date().toString(), group.getCreationDate().toString());
    Assert.assertTrue(group.isModeratorAuthRequired());
  }


  @Test
  public void groupMemberTest() {
    Group  group = new Group();
    group.setgName("atti");

    GroupMember groupMember = new GroupMember();
    groupMember.setGroupId(group);
    groupMember.setCreationDate(new Date());
    groupMember.setModerator(true);

    Assert.assertEquals("atti", groupMember.getGroupId().getgName());
    Assert.assertEquals( new Date().toString(), groupMember.getCreationDate().toString());
    Assert.assertTrue(groupMember.isModerator());
  }

  @Test
  public void userTest() {
    User user = new User();
    user.setEmail("email");
    user.setPassword("pass");
    user.setSuperUser(true);


    Assert.assertEquals("email", user.getEmail());
    Assert.assertEquals("pass", user.getPassword());
    Assert.assertTrue( user.isSuperUser());
    Assert.assertEquals("0\tnull", user.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void userTestNull() {
    User user = new User(null,"password","email");
    User user2 = new User("user",null,"email");
  }

  @Test
  public void userFollow() {

    User user = new User();
    user.setName("atti");

    UserFollow userFollow = new UserFollow();
    userFollow.setFollowerUser(user);
    userFollow.setFollowDate(new Date());


    Assert.assertEquals("atti", userFollow.getFollowerUser().getName());
    Assert.assertEquals( new Date().toString(), userFollow.getFollowDate().toString());

  }



}
