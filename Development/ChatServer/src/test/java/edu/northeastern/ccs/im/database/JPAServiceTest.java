package edu.northeastern.ccs.im.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.NoResultException;

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

    @Test(expected = NoResultException.class)
    public void testHashForUser(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.getHashFromUsername(null);
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

    @After
    public void after() {
        sessionFactory.close();
    }
}
