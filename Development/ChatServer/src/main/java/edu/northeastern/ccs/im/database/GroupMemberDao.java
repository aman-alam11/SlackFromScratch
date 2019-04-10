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

    /**
     * Constants to reuse in place of similar string throughout the code.
     */
    public static final String GROUP_ID = "groupId";
    public static final String GROUP_USER = "groupUser";
    public static final String GROUP_NOT_FOUND = "Group not found :";
    public static final String USER_NOT_FOUND = "User not found :";

    SessionFactory mSessionFactory;

    public GroupMemberDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    /**
     * This method adds a new member to a group.
     *
     * @param gName The name of the group to which the member is added.
     * @param uName The name of the user who is being added to the group.
     * @param isModerator Flag deciding if the added user will be a moderator or not.
     * @return The boolean flag indicating if the operation was successful or failed.
     */
    public boolean addMemberToGroup(String gName, String uName, boolean isModerator){
        boolean isTransactionSuccessful = false;
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = JPAService.getInstance().findUserByName(uName);
            if (user == null) {
                ChatLogger.info(this.getClass().getName() + USER_NOT_FOUND + " " + uName);
                return false;
            }

            Group grp = JPAService.getInstance().findGroupByName(gName);
            if (grp == null) {
                ChatLogger.info(this.getClass().getName() + GROUP_NOT_FOUND + " " + gName);
                return false;
            }
            GroupMember groupMember = new GroupMember(user,grp,isModerator);
            session.save(groupMember);
            transaction.commit();
            isTransactionSuccessful = true;
        } catch (HibernateException ex) {
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }

        return isTransactionSuccessful;
    }

    /**
     * This method deletes a user from the given group.
     *
     * @param gName The name of the group from where the member is removed.
     * @param uName The name of the user who is being removed from group.
     * @return The boolean flag indicating if the operation was successful or failed.
     */
    public boolean deleteMemberFromGroup(String gName, String uName){
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        boolean isTransactionSuccessful = false;

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

            isTransactionSuccessful = true;
            transaction.commit();
        } catch (HibernateException ex) {
            isTransactionSuccessful = false;
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }

        return isTransactionSuccessful;
    }

    /**
     * This is a helper method, which takes in a group name and checks whether or not that group exists.
     *
     * @param gName The name of the group.
     * @return The group with the given name.
     */
    private Group getGroup(String gName) {
        Group grp = JPAService.getInstance().findGroupByName(gName);
        if (grp == null) {
            ChatLogger.info(this.getClass().getName() + GROUP_NOT_FOUND + " " + gName);
            throw new HibernateException("group not found");
        }
        return grp;
    }

    /**
     * This method deletes all the members form the group.
     *
     * @param gName Name of the group from which all the members are deleted.
     */
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
            transaction.commit();
        } catch (HibernateException ex) {
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    /**
     * This method is used to add bunch of users to the group as members.
     *
     * @param users The list of users to be added.
     * @param gName The name of the group to which the users are to be added.
     * @return The boolean flag indicating if the operation was successful or failed.
     */
    public boolean addMultipleUsersToGroup(List<String> users, String gName){
        boolean isTransactionSuccessful = false;
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            for(String u: users){
                JPAService.getInstance().addGroupMember(gName,u,false);
            }
            transaction.commit();
            isTransactionSuccessful = true;
        } catch (HibernateException ex) {
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }

        return isTransactionSuccessful;
    }

    /**
     * This method finds out all the members of a group and returns a list of them.
     *
     * @param gName The name of the group whose members we want to list.
     * @return The list of users who are member of the given group.
     */
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
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }
        return allMembers;
    }

    /**
     * This method changes a moderator status of a user in the group.
     *
     * @param uName The name of the user who is a member of the group and whose moderator status is being updated.
     * @param gName The name of the group.
     * @param moderatorStatus The new moderator status for specified user for the specified group.
     */
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
            transaction.commit();
        } catch (HibernateException ex) {
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }
    }

    /**
     * This is a helper method which takes in a user name and see if that user exists or not.
     *
     * @param uName The name of the user.
     * @return The User object matching with the name given.
     */
    private User getUser(String uName) {
        User user = JPAService.getInstance().findUserByName(uName);
        if (user == null) {
            ChatLogger.info(this.getClass().getName() + USER_NOT_FOUND + " " + uName);
            throw new HibernateException("user not found");
        }
        return user;
    }

    /**
     * This method returns the list of users from given who are not member of the provided group.
     * @param names Provided the list of names of users.
     * @param gName The name of the group.
     * @return The list of users who are not part of the given group from the list of users specified.
     */
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
            transaction.commit();
        } catch (HibernateException ex) {
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
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
            Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
        } finally {
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
            ChatLogger.error(ex.getMessage());
            Objects.requireNonNull(transaction).rollback();
        } finally {
            Objects.requireNonNull(session).close();
        }
        return allMembers;
    }

    /**
     * This method is used to toggle the admin rights of a specified user in the group.
     *
     * @param userId The user id who is a member of the group.
     * @param groupId The group id of a group in which the given user is a member.
     * @return The boolean flag indicating if the operation was successful or failed.
     */
    public boolean toggleAdminRightsOfUser(int userId, long groupId){
        Session session = null;
        Transaction transaction = null;
        boolean isTransSuccess = false;
        try {
            session = mSessionFactory.openSession();
            transaction = session.beginTransaction();

            String sql = "UPDATE group_member SET isModerator =  isModerator ^ 1 WHERE user_Id = ? AND group_id = ?";
            Query query = session.createNativeQuery(sql, GroupMember.class);
            query.setParameter(1, userId);
            query.setParameter(2, groupId);
            query.executeUpdate();

            transaction.commit();
            isTransSuccess= true;
        } catch (HibernateException ex) {
            ChatLogger.error(ex.getMessage());
            Objects.requireNonNull(transaction).rollback();
        } finally {
            Objects.requireNonNull(session).close();
        }
        return isTransSuccess;
    }

    /**
     * Thie method checks if a provided user is a member of a group or not.
     *
     * @param uName The user name of any User.
     * @param gName The name of any group.
     * @return The boolean flag indicating if the given user is a member of given group or not.
     */
    public boolean userIsMember(String uName, String gName){
        boolean isTransactionSuccessful = false;
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = JPAService.getInstance().findUserByName(uName);
            if (user == null) {
                ChatLogger.info(this.getClass().getName() + USER_NOT_FOUND + " " + uName);
                return false;
            }

            Group grp = JPAService.getInstance().findGroupByName(gName);
            if (grp == null) {
                ChatLogger.info(this.getClass().getName() + GROUP_NOT_FOUND + " " + gName);
                return false;
            }

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GroupMember> query = builder.createQuery(GroupMember.class);
            Root<GroupMember> root = query.from(GroupMember.class);
            query.select(root).where(builder.and(builder.equal(root.get(GROUP_ID), grp)),
                    (builder.equal(root.get(GROUP_USER), user)),
                    (builder.equal(root.get("isModerator"), true)));
            Query<GroupMember> q = session.createQuery(query);
            if(!q.getResultList().isEmpty()){
                isTransactionSuccessful = true;
            }
            transaction.commit();
        } catch (HibernateException ex) {
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }

        return isTransactionSuccessful;
    }
}
