package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import edu.northeastern.ccs.im.ChatLogger;

import static edu.northeastern.ccs.im.server.business.logic.handler.SuperUserHandler.END_DATE;
import static edu.northeastern.ccs.im.server.business.logic.handler.SuperUserHandler.START_DATE;

public class GroupDao {
    SessionFactory mSessionFactory;

    public GroupDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    /**
     * This method create a new group.
     * @param gName Name of the group.
     * @param gCreator User creator of the group.
     * @param isAuthRequired Mention if moderator authentication is required to add/remove users from group.
     * @return The boolean flag indicating the success or failure of operation.
     */
    public boolean create(String gName, String gCreator, boolean isAuthRequired){
        boolean isTransactionSuccessful = false;
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            User user = JPAService.getInstance().findUserByName(gCreator);
            if (user == null) {
            	ChatLogger.info(this.getClass().getName() + "User not found : " + gCreator);
            	return false;
            }
            Group grp = new Group(gName, user, isAuthRequired);
            session.save(grp);
            GroupMember groupMember = new GroupMember(user,grp,true);
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
     * Delete a group.
     * @param id The id of the group that needs to be deleted.
     * @return The boolean flag indicating the success or failure of operation.
     */
    public boolean delete(long id) {
        Session session = mSessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        boolean isOperationSuccess = false;
        try {
            Group grp = session.get(Group.class, id);
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
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }

        return isOperationSuccess;
    }

    /**
     * This method finds a group with the specified name.
     * @param name The string name from which matching group with that name is to be found.
     * @return The group.
     */
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
            ChatLogger.error(ex.getMessage());
        } finally {
            session.close();
        }
        return grp;
    }

    /**
     * This method finds out all the groups of particular user who is the creator for those groups.
     * @param user The user who is checked to be the creator of the group.
     * @return The list of group whose creator is the given user.
     */
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
            ChatLogger.error(ex.getMessage());
        } finally {
            session.close();
        }
        return grp;
    }

    /**
     * This method returns a list of all the groups whose name matches the query string passed.
     * @param gName The string name to which the matching group names are found.
     * @return The list of all the groups containing the given string in their name.
     */
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

    /**
     * This method updates the group name. Checks before changing if the new name is already being used.
     * @param oldName The old name of the group.
     * @param newName The new name of the group.
     * @return The boolean flag indicating success or failure of the operation.
     */
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
            session.update(grp);
            isTransactionSuccessful = true;

            transaction.commit();
        } catch (HibernateException ex) {
            ChatLogger.error(ex.getMessage());
            transaction.rollback();
        } finally {
            session.close();
        }
        return isTransactionSuccessful;
    }

    /**
     * This method returns the list of groups in which user is a part of.
     * @param uName The username of a user whose groups we want to list.
     * @param gName The string name, matching the group name.
     * @return The list of groups fulfilling the input criteria.
     */
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
     * @param group The group for which the chat messages are needed to be listed.
     * @param dateMap The range of dates between which the messages are needed.
     * @return A List of complete chat rows from which information will be extracted based on use case.
     */
    public List<Chat> getUnreadMessagesForGroup(Group group, Map<String, String> dateMap) {
        Session session = mSessionFactory.openSession();
        List<Chat> allGroupChats = null;
        StringBuilder sqlQueryBuilder = new StringBuilder("SELECT * FROM chat WHERE chat.Group_id=?");
        try {
            if(dateMap != null) {
                sqlQueryBuilder.append(" and chat.Creation_date >= \"");
                sqlQueryBuilder.append(dateMap.get(START_DATE));
                sqlQueryBuilder.append("\" AND chat.Creation_date <= \"");
                sqlQueryBuilder.append(dateMap.get(END_DATE));
                sqlQueryBuilder.append("\"");
            }
            Query query = session.createNativeQuery(sqlQueryBuilder.toString(), Chat.class);
            query.setParameter(1, group.getId());
            allGroupChats = query.getResultList();
        } catch (HibernateException | IllegalArgumentException | NoResultException ex) {
            ChatLogger.error(ex.getMessage());
        } finally {
            session.close();
        }
        return allGroupChats;
    }

}
