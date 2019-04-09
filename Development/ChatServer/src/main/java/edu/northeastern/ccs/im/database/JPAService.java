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
import edu.northeastern.ccs.im.model.FetchLevel;
import edu.northeastern.ccs.im.model.UnreadMessageModel;

public class JPAService {

  /**
   * creating an object of UserDao to call the methods to perform operations on user.
   */
  private UserDao ud;

  private static final String USERNOTFOUND = "User not found : ";

  /**
   * Creating an object of ChatDao to call the methods to perform operations on chat.
   */
  private ChatDao cd;

  private GroupDao gd;

  private GroupMemberDao gmd;

  private UserFollwDao ufd;
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

  /**
   * Only for testing purpose
   */
  public static synchronized void setJPAService(JPAService jpa) {
    instance = jpa;
  }

  // Create the SessionFactory using the ServiceRegistry
  SessionFactory mSessionFactory = new Configuration().
          configure().
          addAnnotatedClass(User.class).
          addAnnotatedClass(Chat.class).
          addAnnotatedClass(Group.class).
          addAnnotatedClass(GroupMember.class).
          addAnnotatedClass(UserFollow.class).
          buildSessionFactory();

  /**
   * Constructor to initialize userdao object.
   */
  private JPAService() {
    ud = new UserDao(mSessionFactory);
    cd = new ChatDao(mSessionFactory);
    gd = new GroupDao(mSessionFactory);
    gmd = new GroupMemberDao(mSessionFactory);
    ufd = new UserFollwDao(mSessionFactory);
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
    ufd = new UserFollwDao(mSessionFactory);
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

  public boolean deleteGroup(String gName) {
    Group g = findGroupByName(gName);
    return gd.delete(g.getId());
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

  public boolean deleteMemberFromGroup(String gName, String uName) {
    return gmd.deleteMemberFromGroup(gName, uName);
  }

  public void deleteAllMembersOfGroup(String gName) {
    gmd.deleteAllMembersFromGroup(gName);
  }

  public boolean addMultipleUsersToGroup(List<String> usersToAdd, String grpToAddTo) {
    return gmd.addMultipleUsersToGroup(usersToAdd, grpToAddTo);
  }

  /**
   * Get all messages based on Fetch Level.
   *
   * @param username   The username to get unread messages for.
   * @param dateMap    The date constraint on the messages that we need to fetch.
   * @param fetchLevel The multiple fetching details that we need to modify queries on.
   * @return A list of Messages which are based on {@link UnreadMessageModel}.
   */
  public List<UnreadMessageModel> getUnreadMessages(String username, Map<String, String> dateMap,
                                                    FetchLevel fetchLevel) {

    Session session = null;
    Transaction transaction = null;
    List<UnreadMessageModel> unreadMessageModels = new ArrayList<>();

    try {
      session = mSessionFactory.openSession();
      transaction = session.beginTransaction();

      Map<String, Integer> validIdMap = isValidIdForUsernameHelper(username);
      if (validIdMap.get(username) == null) {
        return unreadMessageModels;
      }


      List<Chat> listRows = ud.getUnreadMessages(validIdMap.get(username), dateMap, fetchLevel);
      for (Chat listRow : listRows) {
        String fromPersonName = listRow.getFromId().getName();
        Date timestamp = listRow.getCreated();
        String message = listRow.getMsg();
        unreadMessageModels.add(new UnreadMessageModel(fromPersonName, message, timestamp, listRow.getIsGrpMsg()));
      }

      if (fetchLevel == FetchLevel.UNREAD_MESSAGE_HANDLER) {
        ud.setDeliverAllUnreadMessages(username);
      }

      transaction.commit();
    } catch (HibernateException ex) {
      ChatLogger.error(ex.getMessage());
      Objects.requireNonNull(transaction).rollback();
    } finally {
      Objects.requireNonNull(session).close();
    }

    return unreadMessageModels;
  }

  private Map<String, Integer> isValidIdForUsernameHelper(String username) {
    Map<String, Integer> resultMap = new HashMap<>();
    // Get the userId for the user for which we need the username
    BigInteger userIdBigInt = ud.getUserIdFromUserName(username);
    int userId = userIdBigInt.intValue();
    if (userId <= 0) {
      ChatLogger.info(this.getClass().getName() + USERNOTFOUND + username);
      return resultMap;
    }

    resultMap.put(username, userId);
    return resultMap;
  }


  /**
   * Set all the messages to deliver by specific username.
   *
   * @param username - user name of the user you want to set delivery true.
   * @return - True or false according to the result.
   */
  public boolean setDeliveredUnreadMessages(String username) {

    return ud.setDeliverAllUnreadMessages(username);
  }

  /**
   * Set number of messages from some user to user a user to isDelivered. So that other user cannot
   * see the messages.
   *
   * @param toUserName    - user name of the user you want to set delivery true.
   * @param fromUserName  - user name of the user from whom you want to set delivery true.
   * @param numOfMessages - total number of messages to set delivered true.
   * @return - True or false according to the result.
   */
  public int setRollBackMessages(String toUserName, String fromUserName, int numOfMessages) {

    return ud.setRollbackMessage(toUserName, fromUserName, numOfMessages);
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

  public List<Group> allGroupsForUser(String uName, String gName) {
    return gd.allGroupsOfUser(uName, gName);
  }


  /**
   * Get all the groups for a particular user that s/he is in.
   *
   * @param username The username of the user for which we want to extract all the groups for.
   * @return A List of Pair where the key is the group name and the value is a boolean that
   * represents whether s/he is a moderator in that particular group or not.
   */
  public Map<String, Boolean> getAllGroupsForUser(String username) {
    Session session = null;
    Transaction transaction = null;
    Map<String, Boolean> allGroupsForUser = new HashMap<>();
    try {
      session = mSessionFactory.openSession();
      transaction = session.beginTransaction();

      Map<String, Integer> validIdMap = isValidIdForUsernameHelper(username);
      if (validIdMap.get(username) == null) {
        return allGroupsForUser;
      }

      allGroupsForUser = gmd.getAllGroupsForUser(validIdMap.get(username));

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
   * @return A Map where the key is the userName of members of the group and the value is a boolean
   * representing if s/he is a moderator in that group.
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


  public boolean toggleAdminRights(String username, String groupName) {
    Session session = null;
    Transaction transaction = null;
    boolean isOperationSuccessful = false;

    try {
      session = mSessionFactory.openSession();
      transaction = session.beginTransaction();

      // Get the userId for the userName
      BigInteger userIdBigInt = ud.getUserIdFromUserName(username);

      int userId = userIdBigInt.intValue();
      if (userId <= 0) {
        ChatLogger.info(this.getClass().getName() + USERNOTFOUND + username);
        return isOperationSuccessful;
      }

      long groupId = gd.findGroupByName(groupName).getId();
      if (groupId <= 0) {
        ChatLogger.info(this.getClass().getName() + "Group not found : " + groupName);
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

  public boolean renameUpdateGroup(String oldName, String newName) {
    return gd.updateGroupName(oldName, newName);
  }

  public boolean userIsModerator(String uName, String gName) {
    return gmd.userIsMember(uName, gName);
  }

  public boolean addFollower(String uName, String fName) {
    return ufd.addFollower(uName, fName);
  }

  /**
   * Get all messages for a group.
   *
   * @param groupname The group for which we need to fetch messages.
   * @param dateMap   The HashMap that contains the start and end date.
   * @return The List of messages parsed according to the mode  {@link UnreadMessageModel}.
   */
  public List<UnreadMessageModel> getUnreadMessagesForGroup(String groupname, Map<String, String> dateMap) {
    Session session = null;
    Transaction transaction = null;
    List<UnreadMessageModel> unreadMessageModels = new ArrayList<>();

    try {
      session = mSessionFactory.openSession();
      transaction = session.beginTransaction();

      // Get the userId for the user for which we need the username
      Group validGroup = gd.findGroupByName(groupname);
      long groupId = validGroup.getId();
      if (groupId <= 0) {
        ChatLogger.info(this.getClass().getName() + USERNOTFOUND + groupname);
        return unreadMessageModels;
      }

      List<Chat> listRows = gd.getUnreadMessagesForGroup(validGroup, dateMap);
      for (Chat listRow : listRows) {
        String fromPersonName = listRow.getFromId().getName();
        Date timestamp = listRow.getCreated();
        String message = listRow.getMsg();
        unreadMessageModels.add(new UnreadMessageModel(fromPersonName, message, timestamp, listRow.getIsGrpMsg()));
      }

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
   * This is explicitly written so that only admin can call it from server side. This is not called
   * from client side. This is called on special request from government.
   *
   * @param userName The username to be upgraded to superUser.
   */
  public void upgradeToSuperUser(String userName) {
    long id = this.findUserByName(userName).getId();
    ud.setAsSuperUser(id);
  }

  public List<User> getAllFollowers(String uName) {
    return ufd.getAllFollowers(uName);
  }
}