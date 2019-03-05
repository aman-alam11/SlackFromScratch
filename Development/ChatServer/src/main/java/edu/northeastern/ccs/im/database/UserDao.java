package edu.northeastern.ccs.im.database;

import java.util.List;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;

public class UserDao {
	private static final SessionFactory SESSION_FACTORY;
	
	 /**
     * Initialize the SessionFactory instance.
     */
    static {
        // Create a Configuration object.
        Configuration config = new Configuration();
        // Configure using the application resource named hibernate.cfg.xml.
        config.configure();
        // Extract the properties from the configuration file.
        Properties prop = config.getProperties();

        // Create StandardServiceRegistryBuilder using the properties.
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(prop);

        // Build a ServiceRegistry
        ServiceRegistry registry = builder.build();

        // Create the SessionFactory using the ServiceRegistry
        SESSION_FACTORY = new Configuration().
                configure().
                addAnnotatedClass(User.class).
                buildSessionFactory();
    }
    
    /**
     * Create a new user.
     * 
     * @param id
     * @param name
     * @param email
     * @param password
     */
	public void create(String name, String email, String password) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            // Save the User
            session.save(user);
            // Commit the transaction
            transaction.commit();
        } catch (HibernateException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }

    /**
     * Read all the Users.
     *
     * @return a List of Users
     */
    public List<User> readAll() {
        List<User> Users = null;
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            Users = session.createQuery("FROM User").list();
            // Commit the transaction
            transaction.commit();
        } catch (HibernateException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
        return Users;
    }

    /**
     * Delete the existing User.
     *
     * @param id
     */
    public void delete(int id) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            // Get the User from the database.
            User user = (User) session.get(User.class, Integer.valueOf(id));
            // Delete the User
            session.delete(user);
            // Commit the transaction
            transaction.commit();
        } catch (HibernateException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }

    /**
     * Update the existing user.
     * 
     * @param id
     * @param name
     * @param email
     * @param password
     */
    public void update(int id, String name, String email, String password) {
        // Create a session
        Session session = SESSION_FACTORY.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            // Get the User from the database.
            User user = (User) session.get(User.class, Integer.valueOf(id));
            // Change the values
            user.setId(id);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            // Update the User
            session.update(user);

            // Commit the transaction
            transaction.commit();
        } catch (HibernateException ex) {
            // If there are any exceptions, roll back the changes
            if (transaction != null) {
                transaction.rollback();
            }
            // Print the Exception
            ex.printStackTrace();
        } finally {
            // Close the session
            session.close();
        }
    }
    
    /**
     * Find a user by unique key user name.
     * @param name
     * @return
     */
    public User findUserByName(String name) {
    	Session session = SESSION_FACTORY.openSession();
    	String sql = "select *from users where users.user_name = ?";

    	Query query = session.createNativeQuery(sql, User.class);
    	query.setParameter(1, name);
    	User user = (User) query.getSingleResult();
    	
    	return user;
    }
    
    /**
     * Close the session factory.
     */
    public void closeSessionFactory() {
    	SESSION_FACTORY.close();
    }
}
