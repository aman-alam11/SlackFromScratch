package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.NoResultException;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.model.FetchLevel;

import static edu.northeastern.ccs.im.server.business.logic.handler.SuperUserHandler.END_DATE;
import static edu.northeastern.ccs.im.server.business.logic.handler.SuperUserHandler.START_DATE;

public class UserDao {

  private static final String LOG_TAG = UserDao.class.getSimpleName();

  private SessionFactory mSessionFactory;

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
      User user = session.get(User.class, id);
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
    try {
      session = mSessionFactory.openSession();
      String sql = "select * from users where users.user_name = ?";

      Query query = session.createNativeQuery(sql, User.class);
      query.setParameter(1, name);
      user = (User) query.getSingleResult();
    } catch (HibernateException | NoResultException ex) {
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
    } finally {
      session.close();
    }
    return user;
  }

  /**
   * Search user with keyword given
   */
  public List<String> searchUserByName(String name) {
    Session session = mSessionFactory.openSession();
    try {
      String sql = "select users.user_name from users where users.user_name like ?";
      Query query = session.createNativeQuery(sql);
      query.setParameter(1, "%" + name + "%");
      return query.getResultList();
    } catch (Exception ex) {
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
    } finally {
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


  public BigInteger getUserIdFromUserName(String username) {
    Session session = mSessionFactory.openSession();
    BigInteger userId = new BigInteger("-1");
    try {
      String sql = "SELECT users.user_id FROM new_test_hibernate.users WHERE new_test_hibernate.users.user_name =?";

      Query query = session.createNativeQuery(sql);
      query.setParameter(1, username);
      userId = (BigInteger) query.getSingleResult();
    } catch (Exception ex) {
      // If there are any exceptions, roll back the changes
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
    } finally {
      // Close the session
      session.close();
    }
    return userId;
  }

  /**
   * Retrieves all unread messages for the user with the passed user id.
   *
   * @param userId The userId for which we need to get all the unread messages for.
   * @return A List of complete chat rows from which information will be extracted based on use
   * case.
   */
  public List<Chat> getUnreadMessages(int userId, Map<String, String> dateMap, FetchLevel fetchLevel) {
    Session session = mSessionFactory.openSession();
    List<Chat> listUnreadChatRows = new ArrayList<>();
    StringBuilder sqlString = new StringBuilder("SELECT * FROM chat WHERE chat.To_id =?");
    String genSqlString = generateAppropriateSqlString(sqlString, fetchLevel, dateMap);

    try {
      Query query = session.createNativeQuery(genSqlString, Chat.class);
      query.setParameter(1, userId);
      listUnreadChatRows.addAll(query.getResultList());
    } catch (Exception ex) {
      // If there are any exceptions, roll back the changes
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
    } finally {
      // Close the session
      session.close();
    }
    return listUnreadChatRows;
  }

  private String generateAppropriateSqlString(StringBuilder sqlString, FetchLevel fetchLevel, Map<String, String> dateMap) {
    switch (fetchLevel) {
      case FETCH_USER_LEVEL:
        appendDatesIfAvailable(sqlString, dateMap);
        sqlString.append(" and NOT chat.isGrpMsg");
        break;

      case FETCH_GROUP_LEVEL:
        appendDatesIfAvailable(sqlString, dateMap);
        sqlString.append(" and chat.isGrpMsg");
        break;

      case UNREAD_MESSAGE_HANDLER:
        appendDatesIfAvailable(sqlString, dateMap);
        sqlString.append(" AND NOT chat.isDelivered");
        break;

      case FETCH_BOTH_USER_GROUP_LEVEL:
        appendDatesIfAvailable(sqlString, dateMap);
        // Intentional fall through

      default:
        // Dont append anything
        break;
    }

    return sqlString.toString();
  }

  /**
   * Update the StringBuilder Object that ultimately leads to Sql String.
   * @param sqlQueryString The stringbuilder object that contains parts of query.
   * @param dateMap The date map that contains start and end date.
   */
  private void appendDatesIfAvailable(StringBuilder sqlQueryString, Map<String, String> dateMap) {
    if (dateMap == null) {
      System.out.println("Dates Invalid.");
      return;
    }
    sqlQueryString.append(" and chat.Creation_date >= \"");
    sqlQueryString.append(dateMap.get(START_DATE));
    sqlQueryString.append("\" AND chat.Creation_date <= \"");
    sqlQueryString.append(dateMap.get(END_DATE));
    sqlQueryString.append("\"");
  }


  public int setRollbackMessage(String toUser, String fromUser, int numberOfMessages) {
    Session session = null;
    int res = 0;
    try {
      session = mSessionFactory.openSession();
      Transaction transaction = session.beginTransaction();
      BigInteger toUserId = this.getUserIdFromUserName(toUser);
      BigInteger fromUserID = this.getUserIdFromUserName(fromUser);

      if (toUserId.intValue() <= 0 || fromUserID.intValue() <= 0) {
        ChatLogger.info(this.getClass().getName() + "User not found to user : " + toUser);
        ChatLogger.info(this.getClass().getName() + "User not found  from user : " + fromUser);
        return res;
      }
      String sql = "update chat set chat.isDelivered = true where To_id =? and From_user_id=? " +
              "and isDelivered = false order by Creation_date desc limit ?";
      Query query = session.createNativeQuery(sql);
      query.setParameter(1, toUserId.intValue());
      query.setParameter(2, fromUserID.intValue());
      query.setParameter(3, numberOfMessages);
      res = query.executeUpdate();
      ChatLogger.info("Rows Affected: " + res);
      transaction.commit();
    } catch (Exception ex) {
      // If there are any exceptions, roll back the changes
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());

    } finally {
      // Close the session
      session.close();
    }
    return res;
  }

  public boolean setDeliverAllUnreadMessages(String username) {

    Session session = null;
    boolean result = false;
    try {
      session = mSessionFactory.openSession();
      Transaction transaction = session.beginTransaction();

      BigInteger userIdBigInt = this.getUserIdFromUserName(username);
      int userId = userIdBigInt.intValue();
      if (userId <= 0) {
        ChatLogger.info(this.getClass().getName() + "User not found : " + username);
        return false;
      }
      String sql = "UPDATE chat SET chat.isDelivered = true WHERE chat.To_id =?";
      Query query = session.createNativeQuery(sql);
      query.setParameter(1, userId);
      int res = query.executeUpdate();
      ChatLogger.info("Rows Affected: " + res);
      result = true;
      transaction.commit();
    } catch (Exception ex) {
      // If there are any exceptions, roll back the changes
      Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
      result = false;

    } finally {
      // Close the session
      session.close();
    }
    return result;
  }


  /**
   * A simple method to upgrade the user as super user. This method cannot be called from client
   * side and can called only from server side for special requests from government agencies.
   *
   * @param userId The userid of the super user.
   */
  public void setAsSuperUser(long userId) {
    Session session = null;
    try {
      session = mSessionFactory.openSession();
      Transaction transaction = session.beginTransaction();

      String sql = "UPDATE new_test_hibernate.users SET users.is_super_user = true WHERE users.user_id =?";
      Query query = session.createNativeQuery(sql);
      query.setParameter(1, userId);
      session.createNativeQuery(sql);
      query.executeUpdate();
      transaction.commit();
    } catch (Exception e) {
      ChatLogger.info(LOG_TAG + "Unable to update as superUser");
    }
  }
}
