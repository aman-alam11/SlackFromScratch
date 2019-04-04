package edu.northeastern.ccs.im.database;

import edu.northeastern.ccs.im.ChatLogger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UserFollwDao {
    public static final String USER_NOT_FOUND = "User not found :";

    SessionFactory mSessionFactory;

    public UserFollwDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    public boolean addFollower(String uName, String fName){
        boolean isTransactionSuccessful = false;
        // Create a session
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = JPAService.getInstance().findUserByName(uName);
            if (user == null) {
                ChatLogger.info(this.getClass().getName() + USER_NOT_FOUND + " " + uName);
                return false;
            }

            User follower = JPAService.getInstance().findUserByName(fName);
            if (follower == null) {
                ChatLogger.info(this.getClass().getName() + USER_NOT_FOUND + " " + fName);
                return false;
            }

            // Save the GroupMember
            UserFollow uf = new UserFollow(user, follower);
            session.save(uf);
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

        return isTransactionSuccessful;
    }
}
