package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("all")
public class UserDao {

  SessionFactory mSessionFactory;

  public UserDao(SessionFactory sf) {
    mSessionFactory = sf;
  }

  /**
   * Create a new user.
   */
  public boolean create(String name, String email, String password) {
    boolean isTransactionSuccessful = false;
    // Create a session
    Session session = null;
    Transaction transaction = null;
    try {
      session = mSessionFactory.openSession();
      // Begin a transaction
      transaction = session.beginTransaction();
      User user = new User(name, password, email);

      // Save the User
      session.save(user);

      // Commit the transaction
      transaction.commit();
      isTransactionSuccessful = true;
    } catch (HibernateException ex) {
      // Print the Exception
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
      // If there are any exceptions, roll back the changes
      transaction.rollback();
    } finally {
      // Close the session
      session.close();
    }

    return isTransactionSuccessful;
  }

  /**
   * Read all the Users.
   *
   * @return a List of Users
   */
  public List<User> readAll() {
    List<User> users = null;
    // Create a session
    Session session = null;
    Transaction transaction = null;
    try {
      session = mSessionFactory.openSession();
      // Begin a transaction
      transaction = session.beginTransaction();
      users = session.createQuery("FROM User").list();

      // Commit the transaction
      transaction.commit();
    } catch (Exception ex) {
      // Print the Exception
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
      // If there are any exceptions, roll back the changes
      transaction.rollback();
    } finally {
      // Close the session
      session.close();
    }
    return users;
  }

  /**
   * Delete the existing User.
   */
  public void delete(int id) {
    // Create a session
    Session session = mSessionFactory.openSession();
    Transaction transaction = null;
    try {
      // Begin a transaction
      transaction = session.beginTransaction();
      // Get the User from the database.
      User user = session.get(User.class, Integer.valueOf(id));
      // Delete the User
      session.delete(user);
      // Commit the transaction
      transaction.commit();
    } catch (HibernateException | IllegalArgumentException ex) {
      // Print the Exception
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
      // If there are any exceptions, roll back the changes
      transaction.rollback();
    } finally {
      // Close the session
      session.close();
    }
  }

  /**
   * Update the existing user.
   */
  public void update(int id, String name, String email, String password) {
    // Create a session
    Session session = null;
    Transaction transaction = null;
    try {
      session = mSessionFactory.openSession();
      // Begin a transaction
      transaction = session.beginTransaction();

      // Get the User from the database.
      User user = session.get(User.class, id);

      // Change the values
      user.setName(name);
      user.setEmail(email);
      user.setPassword(password);

      // Update the User
      session.update(user);

      // Commit the transaction
      transaction.commit();
    } catch (Exception ex) {
      // Print the Exception
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
      // If there are any exceptions, roll back the changes
      transaction.rollback();
    } finally {
      // Close the session
      session.close();
    }
  }

  /**
   * Find a user by unique key user name.
   */
  public User findUserByName(String name) {
    Session session = null;
    User user = null;
    try{
      session = mSessionFactory.openSession();
      String sql = "select * from users where users.user_name = ?";

      Query query = session.createNativeQuery(sql, User.class);
      query.setParameter(1, name);
      user = (User) query.getSingleResult();
    }catch (HibernateException | NoResultException ex){
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
    }finally {
      session.close();
    }
    return user;
  }

  /**
   * Search user with keyword given
   * @param name
   * @return
   */
  public List<String> searchUserByName(String name) {
	    Session session = mSessionFactory.openSession();
	    try{
	      String sql = "select users.user_name from users where users.user_name like ?";
	      Query query = session.createNativeQuery(sql);
	      query.setParameter(1, "%"+ name+ "%");
	      return query.getResultList();
	    }catch (Exception ex){
	      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
	    }finally {
	      session.close();
	    }
	    return null;
	  }

  public String findHashForUsername(String username) {
    Session session = mSessionFactory.openSession();

    try {
      String sql = "SELECT users.user_password FROM users WHERE users.user_name = ?";

      Query query = session.createNativeQuery(sql);
      query.setParameter(1, username);
      return (String) query.getSingleResult();
    } catch (HibernateException | NoResultException ex) {
      // If there are any exceptions, roll back the changes
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
    } finally {
      // Close the session
      session.close();
    }
    return "";
  }
}
