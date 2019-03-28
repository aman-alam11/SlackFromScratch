package edu.northeastern.ccs.im.database;

import edu.northeastern.ccs.im.ChatLogger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DatabaseTest {
    private SessionFactory sessionFactory;

    @Before
    public void before() {
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
    public void testUpdateChatStatus(){
        JPAService jpaS = new JPAService(sessionFactory);
        jpaS.createUser("Alice","a@a.com","alice");
        jpaS.createUser("Bob","b@b.com","bob");
        jpaS.createChatMessage("Alice", "Bob", "hey there",0, new Date(), false, false);

        jpaS.updateChatStatus(1,true);

        try{
            assertEquals(true,jpaS.findByReceiver("Bob").get(0).getIsDelivered());
            ChatLogger.error(jpaS.findByReceiver("Bob").get(0).getIsDelivered().toString());
        }catch(Exception e){
            ChatLogger.error(e.getMessage());
        }
    }

    @AfterClass
    public static void close() {
        try {
            JPAService jpaS = new JPAService();
            jpaS.closeSessionFactory();
        } catch (Exception e) {
            assertEquals("", e.getMessage());
        }
    }

    @After
    public void after() {
        try {
            sessionFactory.close();
        } catch (Exception e) {
            assertEquals("", e.getMessage());
        }
    }
}
