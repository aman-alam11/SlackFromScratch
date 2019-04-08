package edu.northeastern.ccs.im.database;

import org.hibernate.*;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.model.FetchLevel;
import edu.northeastern.ccs.im.model.UnreadMessageModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GroupDao {
    SessionFactory mSessionFactory;

    public GroupDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    public boolean create(String gName, String gCreator, boolean isAuthRequired){
        boolean isTransactionSuccessful = false;
        // Create a session
        Session session = null;
        session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
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
            transaction.rollback();

        } finally {
            // Close the session
            session.close();

        }

        return isTransactionSuccessful;
    }

    public boolean delete(long id) {
        // Create a session
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        boolean isOperationSuccess = false;
        try {
            // Get the User from the database.
            Group grp = session.get(Group.class, id);
            // Delete the User
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);
            query.select(root).where(builder.equal(root.get("groupId"), grp));
            Query<GroupMember> q = session.createQuery(query);
            List<GroupMember> gMembers = q.getResultList();
            for(GroupMember gm: gMembers){
                session.delete(gm);
            }
            CriteriaBuilder builderChat = session.getCriteriaBuilder();
            CriteriaQuery<Chat> queryChat = builderChat.createQuery(Chat.class);
            Root<Chat> rootChat = queryChat.from(Chat.class);
            queryChat.select(rootChat).where(builderChat.equal(rootChat.get("groupId"), grp));
            Query<Chat> qChat = session.createQuery(queryChat);
            List<Chat> allC = qChat.getResultList();
            for(Chat c: allC){
                session.delete(c);
            }
            session.delete(grp);

            isOperationSuccess = true;
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

        return isOperationSuccess;
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

    public boolean updateGroupName(String oldName, String newName){
        Session session = null;
        session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        boolean isTransactionSuccessful = false;

        try {
            Group grp = JPAService.getInstance().findGroupByName(oldName);
            if (grp == null) {
                ChatLogger.info(this.getClass().getName() + "Group not found : " + oldName);
                 throw new HibernateException("Group not found");
            }

            Group newGrp = JPAService.getInstance().findGroupByName(newName);
            if (newGrp != null) {
                ChatLogger.info(this.getClass().getName() + "Group already exist : " + newName);
                throw new HibernateException("Group with same name found");
            }

            grp.setgName(newName);
            // Save the Group
            session.update(grp);
            isTransactionSuccessful = true;

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
        return isTransactionSuccessful;
    }

    public List<Group> allGroupsOfUser(String uName, String gName){
        Session session = mSessionFactory.openSession();
        List<Group> allGrps = null;
        try{
            User u = JPAService.getInstance().findUserByName(uName);
            if (u == null) {
                ChatLogger.info(this.getClass().getName() + "User not found : " + uName);
                throw new HibernateException("User not found");
            }
            String sql = "select groups.* from groups JOIN group_member  on groups.group_id=group_member.group_id where groups.group_name LIKE ? AND group_member.user_id=?";
            Query query = session.createNativeQuery(sql, Group.class);
            query.setParameter(1, "%"+ gName+ "%");
            query.setParameter(2, u);
            allGrps = query.getResultList();
        }catch (Exception ex){
            ChatLogger.error(ex.getMessage());
        }finally {
            session.close();
        }
        return allGrps;
    }


    /**
     * Retrieves all unread messages for the user with the passed user id.
     *
     * @param userId The userId for which we need to get all the unread messages for.
     * @return A List of complete chat rows from which information will be extracted based on use case.
     */
    public List<Chat> getUnreadMessagesForGroup(Group group, Map<String, Date> dateMap) {
        Session session = mSessionFactory.openSession();
        List<Chat> allGroupChats = null;
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Chat> query = builder.createQuery(Chat.class);
            Root<Chat> rootChat = query.from(Chat.class);

            query.select(rootChat).where(builder.equal(rootChat.get("groupId"), group));
            Query<Chat> q = session.createQuery(query);
            allGroupChats = q.getResultList();
        } catch (HibernateException | IllegalArgumentException | NoResultException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
        } finally {
            // Close the session
            session.close();
        }
        return allGroupChats;
    }

}
