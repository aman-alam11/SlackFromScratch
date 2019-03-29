package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import edu.northeastern.ccs.im.ChatLogger;

public class GroupMemberDao {

    public static final String GROUP_ID = "groupId";
    public static final String GROUP_USER = "groupUser";

    SessionFactory mSessionFactory;

    public GroupMemberDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    public boolean addMemberToGroup(String gName, String uName, boolean isModerator){
        boolean isTransactionSuccessful = false;
        // Create a session
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
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
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = getUser(uName);

            Group grp = getGroup(gName);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);
            query.select(root).where(builder.and(builder.equal(root.get(GROUP_ID), grp)),
                    (builder.equal(root.get(GROUP_USER), user)));
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

    private Group getGroup(String gName) {
        Group grp = JPAService.getInstance().findGroupByName(gName);
        if (grp == null) {
            ChatLogger.info(this.getClass().getName() + "Group not found : " + gName);
            throw new HibernateException("group not found");
        }
        return grp;
    }

    public void deleteAllMembersFromGroup(String gName){
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Group grp = getGroup(gName);

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);
            query.select(root).where(builder.equal(root.get(GROUP_ID), grp));
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
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
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
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Group grp = getGroup(gName);

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);
            query.select(root).where(builder.equal(root.get(GROUP_ID), grp.getId()));
            Query<GroupMember> q = session.createQuery(query);
            List<GroupMember> gMembers = q.getResultList();
            for(GroupMember gm: gMembers){
                allMembers.add(gm.getGroupUser());
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

    public void updateModeratorStatus(String uName, String gName, boolean moderatorStatus){
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = getUser(uName);

            Group grp = getGroup(gName);
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);
            query.select(root).where(builder.and(builder.equal(root.get(GROUP_ID), grp)),
                    (builder.equal(root.get(GROUP_USER), user)));
            Query<GroupMember> q = session.createQuery(query);
            GroupMember gMemberToUpdate = q.getSingleResult();
            gMemberToUpdate.setModerator(moderatorStatus);
            session.update(gMemberToUpdate);
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

    private User getUser(String uName) {
        User user = JPAService.getInstance().findUserByName(uName);
        if (user == null) {
            ChatLogger.info(this.getClass().getName() + "User not found : " + uName);
            throw new HibernateException("user not found");
        }
        return user;
    }

    public List<User> findNonMembers(List<String> names, String gName){
        List<User> nonMembers = new ArrayList<>();
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            List<User> tempUserList = new ArrayList<>();
            Group grp = getGroup(gName);
            for(String name: names){
                tempUserList.add(getUser(name));
            }

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);

            for(User u: tempUserList){
                query.select(root).where(builder.and(builder.equal(root.get(GROUP_ID), grp)),
                        (builder.equal(root.get(GROUP_USER), u)));
                Query<GroupMember> q = session.createQuery(query);
                if(q.getResultList().isEmpty()){
                    nonMembers.add(u);
                }
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
        return nonMembers;
    }

    /**
     * Get all groups for the particular user with passed userid
     *
     * @param userId The userId which is in any group.
     * @return A List of Pair where key is the Group name and value is a boolean which represents whether s/he is a
     * moderator in that group.
     */
    public Map<String, Boolean> getAllGroupsForUser(int userId) {
        Session session = mSessionFactory.openSession();
        List<GroupMember> listGroupForUser = new ArrayList<>();
        Map<String, Boolean> groupNameList = new HashMap<>();

        try {
            String sql = "SELECT * FROM group_member WHERE user_id =?";
            Query query = session.createNativeQuery(sql, GroupMember.class);
            query.setParameter(1, userId);
            listGroupForUser = query.getResultList();
            for(GroupMember member : listGroupForUser){
                groupNameList.put(member.getGroupId().getgName(), member.isModerator());
            }
        } catch (Exception ex) {
            // If there are any exceptions, roll back the changes
            Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
        } finally {
            // Close the session
            session.close();
        }
        return groupNameList;
    }


    /**
     * Retrieves the names of all members of the particular group.
     * @param groupId The groupId of the group for which we need to retrieve all the members for.
     * @return A Map where the username of members is key and value is the property which represents if they are admin or not.
     */
    public Map<String, Boolean> findAllMembersOfGroupAsMap(long groupId){
        Map<String, Boolean> allMembers = new HashMap<>();
        Session session = null;
        Transaction transaction = null;
        try {
            session = mSessionFactory.openSession();
            // Begin a transaction
            transaction = session.beginTransaction();

            String sql = "SELECT * FROM group_member where group_id=?";
            Query query = session.createNativeQuery(sql, GroupMember.class);
            query.setParameter(1, groupId);
            List<GroupMember> listUsersForGroup = (List<GroupMember>) query.getResultList();

            for(GroupMember gm: listUsersForGroup){
                allMembers.put(gm.getGroupUser().getName(), gm.isModerator());
            }

            transaction.commit();
        } catch (HibernateException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
            // If there are any exceptions, roll back the changes
            Objects.requireNonNull(transaction).rollback();
        } finally {
            // Close the session
            Objects.requireNonNull(session).close();
        }
        return allMembers;
    }

    public boolean toggleAdminRightsOfUser(int userId, long groupId){
        Session session = null;
        Transaction transaction = null;
        boolean isTransSuccess = false;
        try {
            session = mSessionFactory.openSession();
            // Begin a transaction
            transaction = session.beginTransaction();

            String sql = "UPDATE group_member SET isModerator =  isModerator ^ 1 WHERE user_Id = ? AND group_id = ?";
            Query query = session.createNativeQuery(sql, GroupMember.class);
            query.setParameter(1, userId);
            query.setParameter(2, groupId);
            query.executeUpdate();

            transaction.commit();
            isTransSuccess= true;
        } catch (HibernateException ex) {
            // Print the Exception
            ChatLogger.error(ex.getMessage());
            // If there are any exceptions, roll back the changes
            Objects.requireNonNull(transaction).rollback();
        } finally {
            // Close the session
            Objects.requireNonNull(session).close();
        }
        return isTransSuccess;
    }

}
