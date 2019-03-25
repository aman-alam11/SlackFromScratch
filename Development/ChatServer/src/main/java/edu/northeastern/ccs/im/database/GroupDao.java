package edu.northeastern.ccs.im.database;

import org.hibernate.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.query.Query;
import java.util.logging.Logger;

public class GroupDao {
    SessionFactory mSessionFactory;

    public GroupDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    public boolean create(String gName, User gCreator){
        boolean isTransactionSuccessful = false;
        // Create a session
        Session session = null;
        Transaction transaction = null;
        try {
            session = mSessionFactory.openSession();
            // Begin a transaction
            transaction = session.beginTransaction();
            Group grp = new Group(gName,gCreator);

            // Save the User
            session.save(grp);

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

    public void delete(int id) {
        // Create a session
        Session session = mSessionFactory.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            // Get the User from the database.
            Group grp = session.get(Group.class, Integer.valueOf(id));
            // Delete the User
            session.delete(grp);
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

    public Group findGroupByName(String name){
        Session session = mSessionFactory.openSession();
        Group grp = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Group> query = builder.createQuery(Group.class);
            Root<Group> root = query.from(Group.class);
            query.select(root).where(builder.equal(root.get("gName"), name));
            Query<Group> q = session.createQuery(query);
            grp = q.getSingleResult();
        } catch (HibernateException | IllegalArgumentException ex) {
            // Print the Exception
            Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
        } finally {
            // Close the session
            session.close();
        }
        return grp;
    }

    public Group findGroupByCreator(User user){
        Session session = mSessionFactory.openSession();
        Group grp = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Group> query = builder.createQuery(Group.class);
            Root<Group> root = query.from(Group.class);
            query.select(root).where(builder.equal(root.get("gCreator"), user));
            Query<Group> q = session.createQuery(query);
            grp = q.getSingleResult();
        } catch (HibernateException | IllegalArgumentException ex) {
            // Print the Exception
            Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
        } finally {
            // Close the session
            session.close();
        }
        return grp;
    }
}
