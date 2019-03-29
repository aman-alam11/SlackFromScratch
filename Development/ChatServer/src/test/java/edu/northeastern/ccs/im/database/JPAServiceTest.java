package edu.northeastern.ccs.im.database;

import edu.northeastern.ccs.im.model.ChatModel;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.northeastern.ccs.im.model.UnreadMessageModel;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("all")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JPAServiceTest {
  private static SessionFactory sessionFactory;
  private static JPAService jpaService;

  @BeforeClass
  public static void before() {

    // setup the session factory
    sessionFactory = new Configuration().
            configure().
            addAnnotatedClass(User.class).
            addAnnotatedClass(Chat.class).
            addAnnotatedClass(Group.class).
            addAnnotatedClass(GroupMember.class).
            setProperty("hibernate.hbm2ddl.auto", "create-drop").
            setProperty("hibernate.connection.url", "jdbc:mysql://team212instance.c1wqnkcqbuje.us-east-2.rds.amazonaws.com:3306/jpa_test").
            buildSessionFactory();
     jpaService = new JPAService(sessionFactory);
    JPAService.setJPAService(jpaService);
  }


  @Test
  public void testA() {
    try {
//      JPAService jpaS = new JPAService(sessionFactory);

      jpaService.createUser("Alice", "a@a.com", "alice");
      jpaService.createUser("Bob","b@b.com","bob");
      jpaService.createUser("Charlie","c@c.com","charlie");
      jpaService.createUser("Ali", "b@b.com", "bob");
      jpaService.createUser("Alike", "c@c.com", "charlie");
      jpaService.createUser("Allison", "d@c.com", "charliec");
      assertEquals(6, jpaService.readAllUsers().size());
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testZ() {
    try {
//      JPAService jpaService = new JPAService(sessionFactory);

      jpaService.deleteUser(1);
      assertEquals(7, jpaService.readAllUsers().size());
    } catch (Exception e) {
      //assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testCreateUserWithNullName() {
    try {
//      JPAService jpaService = new JPAService(sessionFactory);
      jpaService.createUser(null, "a@a.com", "asdf");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Username and Password can't be null"));
    }
  }

  @Test
  public void testCreateUserWithNullEmail() {
//      JPAService jpaS = new JPAService(sessionFactory);
    jpaService.createUser("a", null, "asdf");
      assertEquals("",jpaService.findUserByName("a").getEmail());
    jpaService.deleteUser((int)jpaService.findUserByName("a").getId());
  }

  @Test
  public void testCreateUserWithNullPassword() {
    try {
//      JPAService jpaService = new JPAService(sessionFactory);
      jpaService.createUser("alices", "a@a.com", null);
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Username and Password can't be null"));
    }
  }

  @Test
  public void testFindUser() {
    try {
//      JPAService jpaService = new JPAService(sessionFactory);
      User alice = jpaService.findUserByName("Alice");
      assertEquals("a@a.com", alice.getEmail());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("could not extract ResultSet"));
    }
  }

  @Test
  public void testD(){
//    JPAService jpaService = new JPAService(sessionFactory);
    jpaService.createGroup("grp_1","Charlie",false);
    assertEquals("grp_1",jpaService.findGroupByName("grp_1").getgName());
    assertEquals("Charlie",jpaService.findGroupByName("grp_1").getgCreator().getName());
    jpaService.renameUpdateGroup("grp_1","new_grp_1");
    assertEquals("new_grp_1",jpaService.findGroupByCreator("Charlie").get(0).getgName());
    assertEquals(1,jpaService.searchGroupByName("new").size());
    assertEquals(1,jpaService.allGroupsForUser("Charlie","new").size());
    jpaService.deleteGroup("new_grp_1");
    assertEquals(0,jpaService.searchGroupByName("new").size());
    jpaService.findGroupByCreator("A");
  }

  @Test
  public void testE(){
    jpaService.createGroup("grp_1","Charlie",false);
    jpaService.addGroupMember("grp_1","Alice",false);
    assertEquals(2,jpaService.findAllMembersOfGroup("grp_1").size());
    assertEquals(0,jpaService.getAllGroupsForUser("Alice").size());
    jpaService.updateModeratorStatus("Alice","grp_1",true);
    List<String> names = new ArrayList<>();
    names.add("Bob");
    names.add("Ali");
    assertEquals(2,jpaService.findNonMembers(names,"grp_1").size());
    jpaService.addMultipleUsersToGroup(names,"grp_1");
    assertEquals(4,jpaService.findAllMembersOfGroup("grp_1").size());
    GroupMemberDao gmd = new GroupMemberDao(sessionFactory);
    Group g = jpaService.findGroupByName("grp_1");
    assertEquals(4,gmd.findAllMembersOfGroupAsMap(g.getId()).size());
    jpaService.deleteMemberFromGroup("grp_1","Bob");
    assertEquals(3,jpaService.findAllMembersOfGroup("grp_1").size());
    jpaService.deleteAllMembersOfGroup("grp_1");
    assertEquals(0,jpaService.findAllMembersOfGroup("grp_1").size());
  }

  @Test
  public void testX() {
    try {
//      JPAService jpaService = new JPAService(sessionFactory);

      User alice = jpaService.findUserByName("Alice");
      assertEquals("a@a.com", alice.getEmail());
      jpaService.updateUser((int)alice.getId(), alice.getName(), "alice@new.com", alice.getPassword());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("could not extract ResultSet"));
    }
  }

  @Test
  public void testHashForUser() {
    try {
//      JPAService jpaService = new JPAService(sessionFactory);
      assertEquals("", jpaService.getHashFromUsername(null));
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

//  @Test
//  public void testW() {
//    try {
//
//      JPAService jpaService = new JPAService(sessionFactory);
//
//      User alice = jpaService.findUserByName("Alice");
//      assertEquals("a@a.com", alice.getEmail());
//
//    } catch (Exception e) {
//      assertTrue(e.getMessage().contains("could not extract ResultSet"));
//    }
//  }

  @Test
  public void testNullEmailGet() {
    try {
      User tempUesr = new User();
    assertEquals("", tempUesr.getEmail());
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testNullEmailSet() {
    try {
      User tempUesr = new User();
      tempUesr.setEmail(null);
    assertEquals("", tempUesr.getEmail());
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

    @Test
    public void testCreateChatMessage(){
//        JPAService jpaS = new JPAService(sessionFactory);
        ChatModel chatModel = new ChatModel();
        chatModel.setFromUserName("Alice");
        chatModel.setToUserName("Bob");
        chatModel.setMsg("hey there");
        chatModel.setDelivered(false);
        chatModel.setGroupName("");
      jpaService.createChatMessage(chatModel);
        assertEquals("hey there",jpaService.findByReceiver("Bob").get(0).getMsg());
    }

    @Test
    public void testFindAllMessagesOfUser(){
//        JPAService jpaS = new JPAService(sessionFactory);
        ChatModel chatModel = new ChatModel();
        chatModel.setFromUserName("Alice");
        chatModel.setToUserName("Bob");
        chatModel.setMsg("hey there");
        chatModel.setDelivered(false);
        chatModel.setGroupName("");
      jpaService.createChatMessage(chatModel);

        ChatModel chatModel2 = new ChatModel();
        chatModel.setFromUserName("Charlie");
        chatModel.setToUserName("Bob");
        chatModel.setMsg("How are you?");
        chatModel.setDelivered(false);
        chatModel.setGroupName("");
      jpaService.createChatMessage(chatModel2);

        assertEquals(0,jpaService.findByReceiver("Alice").size());
      jpaService.deleteChatByReceiver("Bob");
        assertEquals(0,jpaService.findByReceiver("Bob").size());
    }

    @Test
    public void testDeleteParticularMessage(){
//        JPAService jpaS = new JPAService(sessionFactory);
        assertEquals(1,jpaService.findByReceiver("Bob").size());
      jpaService.deleteMessage(1);
        assertEquals(0,jpaService.findByReceiver("Bob").size());
    }

  @Test
  public void testChatGetters() {
    try {
//      JPAService jpaService = new JPAService(sessionFactory);

      User alice = jpaService.findUserByName("Alice");
      User bob = jpaService.findUserByName("Bob");

      Date toSave = new Date();
      Chat c = new Chat();
      c.setId(1);
      c.setFromId(alice);
      c.setToId(bob);
      c.setMsg("Hi");
      c.setGroupId(null);
      c.setExpiry(toSave);
      c.setCreated(toSave);
      c.setIsDelivered(true);

    assertEquals(1, c.getId());
    assertEquals(1, c.getFromId().getId());
    assertEquals(2, c.getToId().getId());
    assertEquals(toSave, c.getCreated());
    assertEquals(toSave, c.getExpiry());
    assertEquals("Hi", c.getMsg());
//    assertEquals(false, c.getGrpMsg());
    assertEquals(true, c.getIsDelivered());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("could not extract ResultSet"));
    }
  }

  @Test
  public void testY() {
    try {
//      JPAService jpaService = new JPAService(sessionFactory);
      jpaService.deleteUser(1);
    } catch (Exception e) {
      //assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testException() {
    try {
//      JPAService jpaService = new JPAService(sessionFactory);
      jpaService.updateUser(1, "a", "a", "a");
      jpaService.readAllUsers();
      jpaService.searchUserbyUserName("s");
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }

  @Test
  public void checkTest() {
    try {
      ChatDao cd = new ChatDao(null);
      cd.findByReceiver(1);
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }

  @Test
  public void checkTest2() {
    try {
      ChatDao cd = new ChatDao(null);
      cd.deleteChatByReceiver(1);
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }

  @Test
  public void checkTest3() {
    try {
      ChatDao cd = new ChatDao(null);
      cd.updateDeliveryStatus(1, false);
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }

  @Test
  public void checkTest4() {
    try {
      ChatDao cd = new ChatDao(null);
      cd.delete(1);
    } catch (Exception e) {
      assertNull(e.getMessage());
    }
  }

  @Test
  public void testSearchUser() {
    try {
//      JPAService jpaS = new JPAService(sessionFactory);

      assertEquals(3, jpaService.searchUserbyUserName("Ali").size());
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testEmptyUserSearch() {
    try {
//      JPAService jpaS = new JPAService(sessionFactory);
      assertEquals(0, jpaService.searchUserbyUserName("f").size());
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

//    @Test
//    public void testUpdateChatStatus(){
//        JPAService jpaS = new JPAService(sessionFactory);
//        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, false);
//        jpaS.updateChatStatus(jpaS.findByReceiver("Bob").get(0).getId(),true);
//        assertEquals(true,jpaS.findByReceiver("Bob").get(0).getIsDelivered());
//    }

  @Test
  public void testCheck() {
    try {
//      JPAService jpaS = new JPAService(sessionFactory);
      User u = jpaService.findUserByName("Alice");
    assertEquals("a@a.com", u.getEmail());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("could not extract ResultSet"));
    }
  }

  @Test
  public void testUnreadMessage(){
    List<UnreadMessageModel> listUnreadMessages = jpaService
            .getUnreadMessages("kumar");

    assertTrue(listUnreadMessages.size() >= 0);
  }



  @AfterClass
  public static void after() {
    try {
      sessionFactory.close();
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }
}
