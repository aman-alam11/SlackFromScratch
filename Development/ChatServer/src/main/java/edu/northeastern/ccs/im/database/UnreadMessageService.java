package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

import edu.northeastern.ccs.im.ChatLogger;

public class UnreadMessageService extends JPAService {

  /**
   * Only for testing purpose
   */
  public UnreadMessageService(SessionFactory sf) {
    super(sf);
  }



  public List<ChatDao> getUnreadMessges(String username) {
    boolean isTransactionSuccessful = false;

    Session session = null;
    Transaction transaction = null;
    List<ChatDao> listRows = null;
    try {
      session = mSessionFactory.openSession();
      // Begin a transaction
      transaction = session.beginTransaction();

      // Get the userId for the user for which we need the username
      int userId = JPAService.getInstance().getUserIdByUserName(username);
      if (userId <= 0) {
        ChatLogger.info(this.getClass().getName() + "User not found : " + username);
        return listRows;
      }

      listRows = JPAService.getInstance().getUnreadMessages(userId);

      // Commit the transaction
      transaction.commit();
      isTransactionSuccessful = true;
    } catch (HibernateException ex) {
      // Print the Exception
      ChatLogger.error(ex.getMessage());
      // If there are any exceptions, roll back the changes
      transaction.rollback();
    } finally {
      // Close the session
      session.close();
    }

    return listRows;
  }

}
