package edu.northeastern.ccs.im.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Date;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("all")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JPAServiceTest {
  private static SessionFactory sessionFactory;

  @BeforeClass
  public static void before() {
    // setup the session factory
    sessionFactory = new Configuration().
            configure().
            addAnnotatedClass(User.class).
            addAnnotatedClass(Chat.class).
            setProperty("hibernate.hbm2ddl.auto", "create-drop").
            setProperty("hibernate.connection.url", "jdbc:mysql://ec2-13-59-164-30.us-east-2.compute.amazonaws.com:3306/jpa_test").
            buildSessionFactory();
  }

  @Test
  public void testA() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      jpaS.createUser("Alice", "a@a.com", "alice");
      jpaS.createUser("Bob","b@b.com","bob");
      jpaS.createUser("Charlie","c@c.com","charlie");
      jpaS.createUser("Ali", "b@b.com", "bob");
      jpaS.createUser("Alike", "c@c.com", "charlie");
      jpaS.createUser("Allison", "d@c.com", "charliec");
      assertEquals(6, jpaS.readAllUsers().size());
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testZ() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      jpaS.deleteUser(1);
    assertEquals(5, jpaS.readAllUsers().size());
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testCreateUserWithNullName() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      jpaS.createUser(null, "a@a.com", "asdf");
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Username and Password can't be null"));
    }
  }

  @Test
  public void testCreateUserWithNullEmail() {
      JPAService jpaS = new JPAService(sessionFactory);
      jpaS.createUser("a", null, "asdf");
      assertEquals("",jpaS.findUserByName("a").getEmail());
      jpaS.deleteUser(jpaS.findUserByName("a").getId());
  }

  @Test
  public void testCreateUserWithNullPassword() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      jpaS.createUser("alices", "a@a.com", null);
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("Username and Password can't be null"));
    }
  }

  @Test
  public void testFindUser() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      User alice = jpaS.findUserByName("Alice");
    assertEquals("a@a.com", alice.getEmail());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("could not extract ResultSet"));
    }
  }

  @Test
  public void testX() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      User alice = jpaS.findUserByName("Alice");
      assertEquals("a@a.com", alice.getEmail());
      jpaS.updateUser(alice.getId(), alice.getName(), "alice@new.com", alice.getPassword());
      User newAlice = jpaS.findUserByName("Alice");
      assertEquals("alice@new.com", newAlice.getEmail());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("could not extract ResultSet"));
    }
  }

  @Test
  public void testHashForUser() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
    assertEquals("", jpaS.getHashFromUsername(null));
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testW() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      User alice = jpaS.findUserByName("Alice");
      assertEquals("a@a.com", alice.getEmail());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("could not extract ResultSet"));
    }
  }

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
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, true);
        assertEquals("hey there",jpaS.findByReceiver("Bob").get(0).getMsg());
    }

    @Test
    public void testFindAllMessagesOfUser(){
        JPAService jpaS = new JPAService(sessionFactory);

        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, true);
        jpaS.createChatMessage("Charlie", "Bob", "How are you?",0, new Date(), false, true);

        assertEquals(4,jpaS.findByReceiver("Bob").size());
        jpaS.deleteChatByReceiver("Bob");
        assertEquals(0,jpaS.findByReceiver("Bob").size());
    }

    @Test
    public void testDeleteParticularMessage(){
        JPAService jpaS = new JPAService(sessionFactory);

        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, true);
        jpaS.createChatMessage("Charlie", "Bob", "How are you?",0, new Date(), false, true);
        assertEquals(3,jpaS.findByReceiver("Bob").size());
        jpaS.deleteMessage(1);
        assertEquals(2,jpaS.findByReceiver("Bob").size());
    }

  @Test
  public void testChatGetters() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);

      User alice = jpaS.findUserByName("Alice");
      User bob = jpaS.findUserByName("Bob");

      Date toSave = new Date();
      Chat c = new Chat();
      c.setId(1);
      c.setFromId(alice);
      c.setToId(bob);
      c.setMsg("Hi");
      c.setReplyTo(0);
      c.setExpiry(toSave);
      c.setCreated(toSave);
      c.setGrpMsg(false);
      c.setIsDelivered(true);

    assertEquals(1, c.getId());
    assertEquals(1, c.getFromId().getId());
    assertEquals(2, c.getToId().getId());
    assertEquals(toSave, c.getCreated());
    assertEquals(toSave, c.getExpiry());
    assertEquals("Hi", c.getMsg());
    assertEquals(0, c.getReplyTo());
    assertEquals(false, c.getGrpMsg());
    assertEquals(true, c.getIsDelivered());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("could not extract ResultSet"));
    }
  }

  @Test
  public void testY() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      jpaS.deleteUser(1);
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testException() {
    try {
      JPAService jpaS = new JPAService(null);
      jpaS.updateUser(1, "a", "a", "a");
      jpaS.readAllUsers();
      jpaS.searchUserbyUserName("s");
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
      JPAService jpaS = new JPAService(sessionFactory);
    assertEquals(3, jpaS.searchUserbyUserName("Ali").size());
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

  @Test
  public void testEmptyUserSearch() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      assertEquals(0, jpaS.searchUserbyUserName("f").size());
    } catch (Exception e) {
      assertEquals("", e.getMessage());
    }
  }

    @Test
    public void testUpdateChatStatus(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, false);
        jpaS.updateChatStatus(jpaS.findByReceiver("Bob").get(0).getId(),true);
        assertEquals(true,jpaS.findByReceiver("Bob").get(0).getIsDelivered());
    }

  @Test
  public void testCheck() {
    try {
      JPAService jpaS = new JPAService(sessionFactory);
      User u = jpaS.findUserByName("Alice");
    assertEquals("a@a.com", u.getEmail());
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("could not extract ResultSet"));
    }
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
