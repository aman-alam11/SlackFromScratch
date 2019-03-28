package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.model.UnreadMessageModel;
import javafx.util.Pair;

public class JPAService {

  /**
   * creating an object of UserDao to call the methods to perform operations on user.
   */
  private UserDao ud;

  /**
   * Creating an object of ChatDao to call the methods to perform operations on chat.
   */
  private ChatDao cd;

  private GroupDao gd;

  private GroupMemberDao gmd;
  /**
   * Initialize the SessionFactory instance.
   */

  private static JPAService instance;


  /**
   * This method returns the instance of JPASercice, since its a singleton object
   *
   * @return {@link JPAService}
   */

  public static synchronized JPAService getInstance() {
    if (instance == null) {
      instance = new JPAService();
    }
    return instance;
  }

  // Create the SessionFactory using the ServiceRegistry
  SessionFactory mSessionFactory = new Configuration().
          configure().
          addAnnotatedClass(User.class).
          addAnnotatedClass(Chat.class).
          addAnnotatedClass(Group.class).
          addAnnotatedClass(GroupMember.class).
          buildSessionFactory();

  /**
   * Constructor to initialize userdao object.
   */
  private JPAService() {
    ud = new UserDao(mSessionFactory);
    cd = new ChatDao(mSessionFactory);
    gd = new GroupDao(mSessionFactory);
    gmd = new GroupMemberDao(mSessionFactory);
  }

  /**
   * Only for testing purpose
   */
  public JPAService(SessionFactory sf) {
    mSessionFactory = sf;
    ud = new UserDao(mSessionFactory);
    cd = new ChatDao(mSessionFactory);
    gd = new GroupDao(mSessionFactory);
    gmd = new GroupMemberDao(mSessionFactory);
  }

  /**
   * Create a new user.
   */
  public boolean createUser(String name, String password) {
    return ud.create(name, null, password);
  }

  public void createUser(String name, String email, String password) {
    ud.create(name, email, password);
  }

  /**
   * Read all users.
   */
  public List<User> readAllUsers() {
    return ud.readAll();
  }

  /**
   * Delete a user.
   */
  public void deleteUser(int id) {
    ud.delete(id);
  }

  /**
   * Update a user.
   */
  public void updateUser(int id, String name, String email, String password) {
    ud.update(id, name, email, password);
  }


  /**
   * Find a user by his user name.
   */
  public User findUserByName(String name) {
    return ud.findUserByName(name);
  }


  public List<String> searchUserbyUserName(String userName) {
    return ud.searchUserByName(userName);
  }

  public String getHashFromUsername(String username) {
    return ud.findHashForUsername(username);
  }

  /**
   * Close a session factory once the operations are done.
   */
  public void closeSessionFactory() {
    mSessionFactory.close();
  }

  /**
   * Create a new message in chat.
   *
   * @return A boolean representing if the transaction was successful or not.
   */
  public long createChatMessage(ChatModel chatModel) {
    User fromUser = findUserByName(chatModel.getFromUserName());
    User toUser = findUserByName(chatModel.getToUserName());
    Group grp = null;
    return cd.create(fromUser, toUser, chatModel);
  }

  /**
   * List all messages for a user or a group.
   */
  public List<Chat> findByReceiver(String username) {
    User user = findUserByName(username);
    return cd.findByReceiver(user.getId());

  }

  /**
   * update the delivery status of chat for a chat id
   */
  public boolean updateChatStatus(long chatId, boolean status) {
    return cd.updateDeliveryStatus(chatId, status);
  }

  /**
   * Delete the chat for a user or a group.
   */
  public void deleteChatByReceiver(String username) {
    User user = findUserByName(username);
    cd.deleteChatByReceiver(user.getId());
  }

  /**
   * Delete a particular message.
   */
  public void deleteMessage(int id) {
    cd.delete(id);
  }

  public boolean createGroup(String gName, String gCreator, boolean isAuthRequired) {

    return gd.create(gName, gCreator, isAuthRequired);
  }

  public Group findGroupByName(String gName) {
    return gd.findGroupByName(gName);
  }

  public void deleteGroup(String gName) {
    Group g = findGroupByName(gName);
    gd.delete(g.getId());
  }

  public List<Group> findGroupByCreator(String name) {
    User user = findUserByName(name);
    return gd.findGroupByCreator(user);
  }

  public List<Group> searchGroupByName(String name) {
    return gd.searchGroupByName(name);
  }

  public boolean addGroupMember(String gName, String uName, boolean isModerator) {
    return gmd.addMemberToGroup(gName, uName, isModerator);
  }

  public void deleteMemberFromGroup(String gName, String uName) {
    gmd.deleteMemberFromGroup(gName, uName);
  }

  public void deleteAllMembersOfGroup(String gName) {
    gmd.deleteAllMembersFromGroup(gName);
  }

  public boolean addMultipleUsersToGroup(List<String> usersToAdd, String grpToAddTo) {
    return gmd.addMultipleUsersToGroup(usersToAdd, grpToAddTo);
  }

  public List<UnreadMessageModel> getUnreadMessages(String username) {

    Session session = null;
    Transaction transaction = null;
    List<UnreadMessageModel> unreadMessageModels = new ArrayList<>();

    try {
      session = mSessionFactory.openSession();
      transaction = session.beginTransaction();

      // Get the userId for the user for which we need the username
      BigInteger userIdBigInt = ud.getUserIdFromUserName(username);
      int userId = userIdBigInt.intValue();
      if (userId <= 0) {
        ChatLogger.info(this.getClass().getName() + "User not found : " + username);
        return unreadMessageModels;
      }

      List<Chat> listRows = ud.getUnreadMessages(userId);
      for (Chat listRow : listRows) {
        String fromPersonName = listRow.getFromId().getName();
        Date timestamp = listRow.getCreated();
        String message = listRow.getMsg();
        unreadMessageModels.add(new UnreadMessageModel(fromPersonName, message, timestamp, listRow.getIsGrpMsg()));
      }

      // Commit the transaction
      transaction.commit();
    } catch (HibernateException ex) {
      ChatLogger.error(ex.getMessage());
      Objects.requireNonNull(transaction).rollback();
    } finally {
      Objects.requireNonNull(session).close();
    }

    return unreadMessageModels;
  }


  /**
   * Set all the messages to deliver by specific username.
   * @param username - user name of the user you want to set delivery true;
   * @return - True or false according to the result.
   */
  public boolean setDeliveredUnreadMessages(String username) {

    return ud.setDeliverAllUnreadMessages(username);
  }


  public List<User> findAllMembersOfGroup(String gName) {
    return gmd.findAllMembersOfGroup(gName);
  }

  public void updateModeratorStatus(String uName, String gName, boolean moderatorStatus) {
    gmd.updateModeratorStatus(uName, gName, moderatorStatus);
  }

  public List<User> findNonMembers(List<String> names, String gName) {
    return gmd.findNonMembers(names, gName);
  }

  public List<Group> allGroupsForUser(String uName, String gName){
    return gd.allGroupsOfUser(uName,gName);
  }


  /**
   * Get all the groups for a particular user that s/he is in.
   *
   * @param username The username of the user for which we want to extract all the groups for.
   * @return A List of Pair where the key is the group name and the value is a boolean that represents
   * whether s/he is a moderator in that particular group or not.
   */
  public Map<String, Boolean> getAllGroupsForUser(String username) {
    Session session = null;
    Transaction transaction = null;
    Map<String, Boolean> allGroupsForUser = new HashMap<>();
    try {
      session = mSessionFactory.openSession();
      transaction = session.beginTransaction();

      // Get the userId for the user for which we need the username
      BigInteger userIdBigInt = ud.getUserIdFromUserName(username);
      int userId = userIdBigInt.intValue();
      if (userId <= 0) {
        ChatLogger.info(this.getClass().getName() + "User not found : " + username);
        return allGroupsForUser;
      }

      allGroupsForUser = gmd.getAllGroupsForUser(userId);

      // Commit the transaction
      transaction.commit();
    } catch (HibernateException ex) {
      ChatLogger.error(ex.getMessage());
      Objects.requireNonNull(transaction).rollback();
    } finally {
      Objects.requireNonNull(session).close();
    }

    return allGroupsForUser;
  }


  /**
   * Gets all the users in a particular group.
   *
   * @param groupName The group name for which we need yo get all the users for.
   * @return A Map where the key is the userName of members of the group and the value is a boolean representing if
   * s/he is a moderator in that group.
   */
  public Map<String, Boolean> getAllUsersForGroup(String groupName) {
    Session session = null;
    Transaction transaction = null;
    Map<String, Boolean> usernameModeratorMap = new HashMap<>();

    try {
      session = mSessionFactory.openSession();
      transaction = session.beginTransaction();

      // Get the userId for the user for which we need the username
      long groupId = gd.findGroupByName(groupName).getId();

      if (groupId <= 0) {
        ChatLogger.info(this.getClass().getName() + "Group not found : " + groupName);
        return usernameModeratorMap;
      }

      usernameModeratorMap = gmd.findAllMembersOfGroupAsMap(groupId);

      transaction.commit();
    } catch (HibernateException ex) {
      ChatLogger.error(ex.getMessage());
      Objects.requireNonNull(transaction).rollback();
    } finally {
      Objects.requireNonNull(session).close();
    }

    return usernameModeratorMap;
  }



  public boolean toggleAdminRights(Pair<String, String> usernameGroupname){
    Session session = null;
    Transaction transaction = null;
    boolean isOperationSuccessful = false;

    try {
      session = mSessionFactory.openSession();
      transaction = session.beginTransaction();

      // Get the userId for the userName
      BigInteger userIdBigInt = ud.getUserIdFromUserName(usernameGroupname.getKey());

      int userId = userIdBigInt.intValue();
      if (userId <= 0) {
        ChatLogger.info(this.getClass().getName() + "User not found : " + usernameGroupname.getKey());
        return isOperationSuccessful;
      }

      long groupId = gd.findGroupByName(usernameGroupname.getValue()).getId();
      if (groupId <= 0) {
        ChatLogger.info(this.getClass().getName() + "Group not found : " + usernameGroupname.getValue());
        return isOperationSuccessful;
      }

      isOperationSuccessful = gmd.toggleAdminRightsOfUser(userId, groupId);

      transaction.commit();
    } catch (HibernateException ex) {
      ChatLogger.error(ex.getMessage());
      Objects.requireNonNull(transaction).rollback();
    } finally {
      Objects.requireNonNull(session).close();
    }

    return isOperationSuccessful;
  }
}