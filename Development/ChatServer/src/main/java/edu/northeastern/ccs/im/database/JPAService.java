package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import edu.northeastern.ccs.im.ChatLogger;
import edu.northeastern.ccs.im.model.UnreadMessageModel;

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
  public long createChatMessage(String fromUserName, String toUserName, String msg, int replyTo,
                                Date expiry, Boolean grpMsg, Boolean isDelivered) {
    User fromUser = findUserByName(fromUserName);
    User toUser = findUserByName(toUserName);
    return cd.create(fromUser, toUser, msg, replyTo, expiry, grpMsg, isDelivered);
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
        unreadMessageModels.add(new UnreadMessageModel(fromPersonName, message, timestamp, listRow.getGrpMsg()));
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


  public List<User> findAllMembersOfGroup(String gName) {
    return gmd.findAllMembersOfGroup(gName);
  }

  public void updateModeratorStatus(String uName, String gName, boolean moderatorStatus) {
    gmd.updateModeratorStatus(uName, gName, moderatorStatus);
  }

  public List<User> findNonMembers(List<String> names, String gName) {
    return gmd.findNonMembers(names, gName);
  }
}
