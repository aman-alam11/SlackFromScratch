package edu.northeastern.ccs.im.database;

import edu.northeastern.ccs.im.ChatLogger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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

    public List<User> getAllFollowers(String uName){
        List<User> allFollowerUsers = new ArrayList<>();
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = JPAService.getInstance().findUserByName(uName);
            if (user == null) {
                ChatLogger.info(this.getClass().getName() + USER_NOT_FOUND + " " + uName);
                throw new HibernateException("User not found");
            }
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<UserFollow> query = builder.createQuery(UserFollow.class);
            Root<UserFollow> root = query.from(UserFollow.class);
            query.select(root).where(builder.equal(root.get("followedUser"), user));
            Query<UserFollow> q = session.createQuery(query);
            List<UserFollow> followers = q.getResultList();
            for(UserFollow followerUser: followers){
                allFollowerUsers.add(followerUser.getFollowerUser());
            }
        } catch (HibernateException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
            // If there are any exceptions, roll back the changes
            transaction.rollback();
        } finally {
            // Close the session
            session.close();
        }

        return allFollowerUsers;
    }
}
