package edu.northeastern.ccs.im.databasetest;

import edu.northeastern.ccs.im.database.Chat;
import edu.northeastern.ccs.im.database.JPAService;
import edu.northeastern.ccs.im.database.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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

    @After
    public void after() {
        sessionFactory.close();
    }
}
