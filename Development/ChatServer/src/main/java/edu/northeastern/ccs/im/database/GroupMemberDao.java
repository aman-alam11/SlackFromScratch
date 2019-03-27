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
import java.util.List;

public class GroupMemberDao {
    SessionFactory mSessionFactory;

    public GroupMemberDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    public boolean addMemberToGroup(String gName, String uName, boolean isModerator){
        boolean isTransactionSuccessful = false;
        // Create a session
        Session session = null;
        Transaction transaction = null;
        try {
            session = mSessionFactory.openSession();
            // Begin a transaction
            transaction = session.beginTransaction();
            User user = JPAService.getInstance().findUserByName(uName);
            if (user == null) {
                ChatLogger.info(this.getClass().getName() + "User not found : " + uName);
                return false;
            }

            Group grp = JPAService.getInstance().findGroupByName(gName);
            if (grp == null) {
                ChatLogger.info(this.getClass().getName() + "Group not found : " + gName);
                return false;
            }
            // Save the GroupMember
            GroupMember groupMember = new GroupMember(user,grp,isModerator);
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

    public void deleteMemberFromGroup(String gName, String uName){
        Session session = null;
        Transaction transaction = null;
        try {
            session = mSessionFactory.openSession();
            // Begin a transaction
            transaction = session.beginTransaction();
            User user = JPAService.getInstance().findUserByName(uName);
            if (user == null) {
                ChatLogger.info(this.getClass().getName() + "User not found : " + uName);
                throw new HibernateException("user not found");
            }

            Group grp = JPAService.getInstance().findGroupByName(gName);
            if (grp == null) {
                ChatLogger.info(this.getClass().getName() + "Group not found : " + gName);
                throw new HibernateException("group not found");
            }
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);
            query.select(root).where(builder.and(builder.equal(root.get("groupId"), grp)),
                    (builder.equal(root.get("groupUser"), user)));
            Query<GroupMember> q = session.createQuery(query);
            GroupMember gMemberToDelete = q.getSingleResult();
            session.delete(gMemberToDelete);
            // Commit the transaction
            transaction.commit();
        } catch (HibernateException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
            // If there are any exceptions, roll back the changes
            transaction.rollback();
        } finally {
            // Close the session
            session.close();
        }
    }

    public void deleteallMembersFromGroup(String gName){
        Session session = null;
        Transaction transaction = null;
        try {
            session = mSessionFactory.openSession();
            // Begin a transaction
            transaction = session.beginTransaction();

            Group grp = JPAService.getInstance().findGroupByName(gName);
            if (grp == null) {
                ChatLogger.info(this.getClass().getName() + "Group not found : " + gName);
                throw new HibernateException("group not found");
            }

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
        } catch (HibernateException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
            // If there are any exceptions, roll back the changes
            transaction.rollback();
        } finally {
            // Close the session
            session.close();
        }
    }

    public boolean addMultipleUsersToGroup(List<String> users, String gName){
        boolean isTransactionSuccessful = false;
        // Create a session
        Session session = null;
        Transaction transaction = null;
        try {
            session = mSessionFactory.openSession();
            // Begin a transaction
            transaction = session.beginTransaction();

            for(String u: users){
                JPAService.getInstance().addGroupMember(gName,u,false);
            }
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

    public List<User> findAllMembersOfGroup(String gName){
        List<User> allMembers = new ArrayList<>();
        Session session = null;
        Transaction transaction = null;
        try {
            session = mSessionFactory.openSession();
            // Begin a transaction
            transaction = session.beginTransaction();
            Group grp = JPAService.getInstance().findGroupByName(gName);
            if(grp == null){
                ChatLogger.info(this.getClass().getName() + "Group not found : " + gName);
                throw new HibernateException("group not found");
            }

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);
            query.select(root).where(builder.equal(root.get("groupId"), grp));
            Query<GroupMember> q = session.createQuery(query);
            List<GroupMember> gMembers = q.getResultList();
            for(GroupMember gm: gMembers){
                allMembers.add(session.get(User.class,gm.getGroupUser()));
            }
            transaction.commit();
        } catch (HibernateException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
            // If there are any exceptions, roll back the changes
            transaction.rollback();
        } finally {
            // Close the session
            session.close();
        }
        return allMembers;
    }
}
