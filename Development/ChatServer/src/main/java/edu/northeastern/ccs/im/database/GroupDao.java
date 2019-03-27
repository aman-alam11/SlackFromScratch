package edu.northeastern.ccs.im.database;

import org.hibernate.*;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.query.Query;

import edu.northeastern.ccs.im.ChatLogger;

import java.util.List;

public class GroupDao {
    SessionFactory mSessionFactory;

    public GroupDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    public boolean create(String gName, String gCreator, boolean isAuthRequired){
        boolean isTransactionSuccessful = false;
        // Create a session
        Session session = null;
        Transaction transaction = null;
        try {
            session = mSessionFactory.openSession();
            // Begin a transaction
            transaction = session.beginTransaction();
            User user = JPAService.getInstance().findUserByName(gCreator);
            if (user == null) {
            	ChatLogger.info(this.getClass().getName() + "User not found : " + gCreator);
            	return false;
            }
            Group grp = new Group(gName, user, isAuthRequired);
            // Save the Group
            session.save(grp);
            GroupMember groupMember = new GroupMember(user,grp,true);
            session.save(groupMember);
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

    public void delete(long id) {
        // Create a session
        Session session = mSessionFactory.openSession();
        Transaction transaction = null;
        try {
            // Begin a transaction
            transaction = session.beginTransaction();
            // Get the User from the database.
            Group grp = session.get(Group.class, id);
            // Delete the User
            session.delete(grp);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);
            query.select(root).where(builder.equal(root.get("groupId"), grp));
            Query<GroupMember> q = session.createQuery(query);
            List<GroupMember> gMembers = q.getResultList();
            for(GroupMember gm: gMembers){
                session.delete(gm);
            }
            // Commit the transaction
            transaction.commit();
        } catch (HibernateException | IllegalArgumentException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
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
        } catch (HibernateException | IllegalArgumentException | NoResultException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
        } finally {
            // Close the session
            session.close();
        }
        return grp;
    }

    public List<Group> findGroupByCreator(User user){
        Session session = mSessionFactory.openSession();
        List<Group> grp = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Group> query = builder.createQuery(Group.class);
            Root<Group> root = query.from(Group.class);
            query.select(root).where(builder.equal(root.get("gCreator"), user));
            Query<Group> q = session.createQuery(query);
            grp = q.getResultList();
        } catch (HibernateException | IllegalArgumentException | NoResultException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
        } finally {
            // Close the session
            session.close();
        }
        return grp;
    }

    public List<Group> searchGroupByName(String gName) {
        List<Group> allGrps = null;
        Session session = mSessionFactory.openSession();
        try{
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Group> query = builder.createQuery(Group.class);
            Root<Group> root = query.from(Group.class);
            query.select(root).where(builder.like(root.get("gName"), "%"+gName+"%"));
            Query<Group> q = session.createQuery(query);
            allGrps = q.getResultList();
        }catch (Exception ex){
            ChatLogger.error(ex.getMessage());
        }finally {
            session.close();
        }
        return allGrps;
    }
}
