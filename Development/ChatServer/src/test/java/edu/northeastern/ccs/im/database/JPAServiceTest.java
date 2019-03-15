package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.PersistenceException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class JPAServiceTest {
    private SessionFactory sessionFactory;

    @Before
    public void before() {
        // setup the session factory
        sessionFactory = new Configuration().
                configure().
                addAnnotatedClass(User.class).
                addAnnotatedClass(Chat.class).
                setProperty("hibernate.hbm2ddl.auto","create-drop").
                setProperty("hibernate.connection.url", "jdbc:mysql://ec2-13-59-164-30.us-east-2.compute.amazonaws.com:3306/jpa_test").
                buildSessionFactory();
    }

    @Test
    public void testCreateUser() {
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice","a@a.com","alice");
        assertEquals(1,jpaS.readAllUsers().size());
    }

    @Test
    public void testDeleteUser(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice","a@a.com","alice");
        jpaS.deleteUser(1);
        assertEquals(0,jpaS.readAllUsers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithNullName(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser(null,"a@a.com","asdf");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateUserWithNullPassword(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("alice","a@a.com",null);
    }

    @Test
    public void testFindUser(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice","a@a.com","alice");
        User alice = jpaS.findUserByName("Alice");
        assertEquals("a@a.com",alice.getEmail());
    }

    @Test
    public void testUpdateUser(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice","a@a.com","alice");
        User alice = jpaS.findUserByName("Alice");
        assertEquals("a@a.com",alice.getEmail());
        jpaS.updateUser(alice.getId(),alice.getName(),"alice@new.com",alice.getPassword());
        User newAlice = jpaS.findUserByName("Alice");
        assertEquals("alice@new.com",newAlice.getEmail());
    }

    @Test
    public void testHashForUser(){
        JPAService jpaS = new JPAService(sessionFactory);
        assertEquals("",jpaS.getHashFromUsername(null));
    }

    @Test
    public void testNullUserEmail(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice",null,"alice");
        User alice = jpaS.findUserByName("Alice");
        assertEquals("", alice.getEmail());
    }

    @Test
    public void testNullEmailGet(){
        User tempUesr = new User();
        assertEquals("",tempUesr.getEmail());
    }

    @Test
    public void testNullEmailSet(){
        User tempUesr = new User();
        tempUesr.setEmail(null);
        assertEquals("",tempUesr.getEmail());
    }

//    @Test
//    public void testCreateChatMessage(){
//        JPAService jpaS = new JPAService(sessionFactory);
//        jpaS.createUser("Alice","a@a.com","alice");
//        jpaS.createUser("Bob","b@b.com","bob");
//        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, true);
//        assertEquals("hey there",jpaS.findByReceiver("Bob").get(0).getMsg());
//    }

//    @Test
//    public void testFindAllMessagesOfUser(){
//        JPAService jpaS = new JPAService(sessionFactory);
//        jpaS.createUser("Alice","a@a.com","alice");
//        jpaS.createUser("Bob","b@b.com","bob");
//        jpaS.createUser("Charlie","c@c.com","charlie");
//
//        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, true);
//        jpaS.createChatMessage("Charlie", "Bob", "How are you?",0, new Date(), false, true);
//
//        assertEquals(2,jpaS.findByReceiver("Bob").size());
//        jpaS.deleteChatByReceiver("Bob");
//        assertEquals(0,jpaS.findByReceiver("Bob").size());
//    }

//    @Test
//    public void testDeleteParticularMessage(){
//        JPAService jpaS = new JPAService(sessionFactory);
//        jpaS.createUser("Alice","a@a.com","alice");
//        jpaS.createUser("Bob","b@b.com","bob");
//
//        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, true);
//        jpaS.createChatMessage("Charlie", "Bob", "How are you?",0, new Date(), false, true);
//        assertEquals(1,jpaS.findByReceiver("Bob").size());
//        jpaS.deleteMessage(1);
//        assertEquals(0,jpaS.findByReceiver("Bob").size());
//    }

    @Test
    public void testChatGetters(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice","a@a.com","alice");
        jpaS.createUser("Bob","b@b.com","bob");

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

        assertEquals(1,c.getId());
        assertEquals(1,c.getFromId().getId());
        assertEquals(2,c.getToId().getId());
        assertEquals(toSave,c.getCreated());
        assertEquals(toSave,c.getExpiry());
        assertEquals("Hi",c.getMsg());
        assertEquals(0,c.getReplyTo());
        assertEquals(false,c.getGrpMsg());
        assertEquals(true,c.getIsDelivered());
    }

    @Test
    public void testDeleteNonExisting(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.deleteUser(1);
    }

    @Test(expected = NullPointerException.class)
    public void testException(){
        JPAService jpaS = new JPAService(null);
        jpaS.updateUser(1,"a","a","a");
        jpaS.readAllUsers();
        jpaS.searchUserbyUserName("s");
    }

    @Test(expected = NullPointerException.class)
    public void checkTest(){
        ChatDao cd = new ChatDao(null);
        cd.findByReceiver(1);
    }

    @Test(expected = NullPointerException.class)
    public void checkTest2(){
        ChatDao cd = new ChatDao(null);
        cd.deleteChatByReceiver(1);
    }

    @Test(expected = NullPointerException.class)
    public void checkTest3(){
        ChatDao cd = new ChatDao(null);
        cd.updateDeliveryStatus(1,false);
    }

    @Test(expected = NullPointerException.class)
    public void checkTest4(){
        ChatDao cd = new ChatDao(null);
        cd.delete(1);
    }

    @Test
    public void testSearchUser(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice","a@a.com","alice");
        jpaS.createUser("Ali","b@b.com","bob");
        jpaS.createUser("Alike","c@c.com","charlie");
        jpaS.createUser("Allison","d@c.com","charliec");

        assertEquals(3,jpaS.searchUserbyUserName("Ali").size());
    }

    @Test
    public void testEmptyUserSearch(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice","a@a.com","alice");
        assertEquals(0,jpaS.searchUserbyUserName("f").size());
    }

//    @Test
//    public void testUpdateChatStatus(){
//        JPAService jpaS = new JPAService(sessionFactory);
//        jpaS.createUser("Alice","a@a.com","alice");
//        jpaS.createUser("Bob","b@b.com","bob");
//        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, false);
//
//        jpaS.updateChatStatus(1,true);
//
//        assertEquals(true,jpaS.findByReceiver("Bob").get(0).getIsDelivered());
//    }

    @Test
    public void testCheck(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice","a@a.com","alice");
        User u = jpaS.findUserByName("Alice");
        assertEquals("a@a.com",u.getEmail());
    }
    @AfterClass
    public static void close(){
        JPAService jpaS = new JPAService();
        jpaS.closeSessionFactory();
    }

    @After
    public void after() {
        sessionFactory.close();
    }
}
