package edu.northeastern.ccs.im.database;



import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.hibernate.SessionFactory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import edu.northeastern.ccs.im.model.ChatModel;
import edu.northeastern.ccs.im.model.FetchLevel;
import static org.mockito.Mockito.when;

public class JPAServiceMockTest {

  @Mock
  SessionFactory sessionFactoryMock;

  @Mock
  Session session;

  @Mock
  CriteriaBuilder criteriaBuilder;

  @Mock
  Root<Group> groupRoot;


  @Mock
  Root<UserFollow> userFollowRoot;

  @Mock
  Root<GroupMember> groupMemberRoot;

  @Mock
  Query query;

  @Mock
  NativeQuery nativeQuery;

  @Mock
  CriteriaQuery<Group> groupCriteriaQuery;

  @Mock
  CriteriaQuery<GroupMember> groupMemberCriteriaQuery;

  @Mock
  CriteriaQuery<UserFollow> userFollowCriteriaQuery;

  @Mock
  Query<Group> groupQuery;

  @Mock
  Query<Chat> chatQuery;

  @Mock
  Query<GroupMember> groupMemberQuery;

  @Mock
  Query<UserFollow> userFollowQuery;

  @Mock
  Transaction transaction;

  private JPAService jpaService;

  @Before
  public void init() {
    MockitoAnnotations.initMocks(this);
    jpaService = new JPAService(sessionFactoryMock);
  }

  @After
  public void close() {
    jpaService.closeSessionFactory();
  }



  @Test
  public void jpaCreateUserTest() {
    String name = "atti";
    String pass = "atti";
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    Assert.assertTrue(jpaService.createUser(name, pass));
  }

  @Test
  public void jpaCreateUserEmailTest() {
    String name = "atti";
    String email = "email";
    String pass = "atti";
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    jpaService.createUser(name, email, pass);
  }

  @Test
  public void jpareadAllUsersTest() {
    List<User> list = new ArrayList<>();
    User user = new User();
    user.setName("atti");
    list.add(user);
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    Assert.assertNull(jpaService.readAllUsers());
  }

  @Test
  public void deleteUserTest() {
    User user = new User();
    user.setId(1);
    user.setName("atti");
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.get(Matchers.anyString(), Matchers.any())).thenReturn(user);
    jpaService.deleteUser(1);
  }




  @Test
  public void updateUserTest() {
    User user = new User();
    user.setId(1);
    user.setName("atti");
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.get(Matchers.anyString(), Matchers.any())).thenReturn(user);
    jpaService.updateUser(1,"atti","email","pass");
  }

  @Test
  public void findByUserNameTest() {
    User user = new User();
    user.setId(1);
    user.setName("atti");
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);
    Assert.assertEquals("atti",jpaService.findUserByName("atti").getName());
  }


  @Test
  public void searchByUserNameTest() {
    List<User> userList = new ArrayList<>();
    User user = new User();
    user.setId(1);
    user.setName("atti");
    userList.add(user);
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getResultList()).thenReturn(userList);
    Assert.assertEquals(1,jpaService.searchUserbyUserName("atti").size());
  }


  @Test
  public void getHashFromUserNameTest() {
    String hash = "123";
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(hash);
    Assert.assertEquals(hash, jpaService.getHashFromUsername("atti"));
  }



  @Test
  public void createChatMessageTest() {

    User fromUser = new User();
    User toUser = new User();

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //From user
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(fromUser);

    //ToUser
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(toUser);

    Assert.assertEquals(0, JPAService.getInstance().createChatMessage(new ChatModel()));
  }


  @Test
  public void findByReceiverTest() {
    User fromUser = new User();

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(fromUser);

    Assert.assertEquals(0, JPAService.getInstance().findByReceiver("atti").size());

  }

  @Test
  public void updateChatStatusTest() {
    Chat chat = new Chat();

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.get(Matchers.<Class<Chat>>any(), Matchers.anyInt())).thenReturn(chat);
    Assert.assertTrue(JPAService.getInstance().updateChatStatus(1, true));
  }

  @Test
  public void deleteChatByReceriverTest() {
    User fromUser = new User();
    Chat chat = new Chat();

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //From user
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(fromUser);

    JPAService.getInstance().deleteChatByReceiver("an");
  }

  @Test
  public void deleteMessageTest() {
    Chat chat = new Chat();

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //From user
    when(session.get(Matchers.<Class<Object>>any(), Matchers.anyInt())).thenReturn(chat);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    Mockito.doNothing().when(session).delete(chat);
    JPAService.getInstance().deleteMessage(1);
  }

  @Test
  public void createGroupTest() {
    User fromUser = new User();

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //From user
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(fromUser);

    Assert.assertTrue(JPAService.getInstance().createGroup("name", "create", false));

    when(nativeQuery.getSingleResult()).thenReturn(null);

    Assert.assertFalse(JPAService.getInstance().createGroup("name", "create", false));
  }

  @Test
  public void findGroupByNameTest() {
    Group group = new Group();
    group.setId(1);

    when(sessionFactoryMock.openSession()).thenReturn(session);

    //From user
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.<Class<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.<Class<Group>>any())).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.<CriteriaQuery<Group>>any())).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);

    Assert.assertEquals(1, JPAService.getInstance().findGroupByName("name").getId());
  }


  @Test
  public void findGroupByCreatorTest() {

    List<Group> groupList = new ArrayList<>();

    Group group = new Group();
    group.setId(1);
    groupList.add(group);
    User user = new User();
    user.setId(2);

    when(sessionFactoryMock.openSession()).thenReturn(session);


    //From user
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);


    //From user
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.<Class<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.<Class<Group>>any())).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.<CriteriaQuery<Group>>any())).thenReturn(groupQuery);
    when(groupQuery.getResultList()).thenReturn(groupList);

    Assert.assertEquals(1, JPAService.getInstance().findGroupByCreator("name").size());
  }


  @Test
  public void deleteGroupTest() {
    Group group = new Group();
    group.setId(1);

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);



    //From user
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.<Class<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.<Class<Group>>any())).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.<CriteriaQuery<Group>>any())).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);
    when(session.get(Matchers.<Class<Object>>any(), Matchers.anyInt())).thenReturn(group);
    Assert.assertTrue(JPAService.getInstance().deleteGroup("name"));
  }

  @Test
  public void searchGroupByNameTest() {
    List<Group> groupList = new ArrayList<>();

    Group group = new Group();
    group.setId(1);
    groupList.add(group);
    when(sessionFactoryMock.openSession()).thenReturn(session);

    //From user
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.<Class<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.<Class<Group>>any())).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.<CriteriaQuery<Group>>any())).thenReturn(groupQuery);
    when(groupQuery.getResultList()).thenReturn(groupList);
    Assert.assertEquals(1,JPAService.getInstance().searchGroupByName("name").size());
  }


  @Test
  public void addGroupMemberTest() {

    User user = new User();
    user.setId(1);

    Group group = new Group();
    group.setId(2);

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Find user by user name
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);

    // Find group by name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.<Class<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.<Class<Group>>any())).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.<CriteriaQuery<Group>>any())).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);

    Assert.assertTrue(JPAService.getInstance().addGroupMember("str","str",true));

  }


  @Test
  public void deleteMemberFromGroupTest() {

    User user = new User();
    user.setId(1);

    Group group = new Group();
    group.setId(2);
    group.setModeratorAuthRequired(true);

    GroupMember groupMember = new GroupMember();
    groupMember.setGroupUser(user);

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Find user by user name
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);


    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);

    // Find group Member by name
    when(criteriaBuilder.createQuery(Matchers.eq(GroupMember.class))).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.from(Matchers.eq(GroupMember.class))).thenReturn(groupMemberRoot);
    when(groupMemberCriteriaQuery.select(Matchers.<Root<GroupMember>>any())).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupMemberCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupMemberCriteriaQuery))).thenReturn(groupMemberQuery);
    when(groupMemberQuery.getSingleResult()).thenReturn(groupMember);
    Mockito.doNothing().when(session).delete(Matchers.any());

    Assert.assertTrue(JPAService.getInstance().deleteMemberFromGroup("str","str"));

  }


  @Test
  public void deleteAllMembersOfGroupTest() {
    User user = new User();
    user.setId(1);

    List<GroupMember> groupMembersList = new ArrayList<>();



    Group group = new Group();
    group.setId(2);
    group.setModeratorAuthRequired(true);

    GroupMember groupMember = new GroupMember();
    groupMember.setGroupUser(user);
    groupMembersList.add(groupMember);

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);


    // Find group Member by name
    when(criteriaBuilder.createQuery(Matchers.eq(GroupMember.class))).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.from(Matchers.eq(GroupMember.class))).thenReturn(groupMemberRoot);
    when(groupMemberCriteriaQuery.select(Matchers.<Root<GroupMember>>any())).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupMemberCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupMemberCriteriaQuery))).thenReturn(groupMemberQuery);
    when(groupMemberQuery.getResultList()).thenReturn(groupMembersList);
    Mockito.doNothing().when(session).delete(Matchers.any());

    JPAService.getInstance().deleteAllMembersOfGroup("str");


  }



  @Test
  public void addMultipleUsersToGroupTest() {

    User user = new User();
    user.setId(1);

    Group group = new Group();
    group.setId(2);

    List<String> strings = new ArrayList<>();
    strings.add("abc");


    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);


    //Find user by user name
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);

    // Find group by name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.<Class<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.<Class<Group>>any())).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.<CriteriaQuery<Group>>any())).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);

    Assert.assertTrue(JPAService.getInstance().addMultipleUsersToGroup(strings, "group"));


  }


  @Test
  public void getUnreadMessagesTest() {
    User user = new User();

    Chat chat = new Chat();
    chat.setFromId(user);
    chat.setCreated(new Date());
    chat.setMsg("hello");
    chat.setIsGrpMsg(true);

    BigInteger userId = BigInteger.ONE;

    List<Chat> chats = new ArrayList<>();

    chats.add(chat);



    Map<String, String> dateMap = new HashMap<>();

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);


    //Get user from UserID
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<Chat>>any())).thenReturn(nativeQuery);
    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.setParameter(Matchers.anyInt(), Matchers.<Class<Chat>>any())).thenReturn(nativeQuery);
    when(nativeQuery.getSingleResult()).thenReturn(userId);
    when(nativeQuery.getResultList()).thenReturn(chats);

    Assert.assertEquals(1, JPAService.getInstance().getUnreadMessages("user", dateMap, FetchLevel.FETCH_USER_LEVEL).size());

  }


  @Test
  public void setDelivertedUnreadMessageTest() {
    BigInteger userId = BigInteger.ONE;

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Get user from UserID
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(userId);
    when(query.executeUpdate()).thenReturn(1);

    Assert.assertTrue(JPAService.getInstance().setDeliveredUnreadMessages("user"));

  }


  @Test
  public void setRollBackMessageTest() {
    BigInteger userId = BigInteger.ONE;

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Get user from UserID
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(userId);
    when(query.executeUpdate()).thenReturn(1);

    Assert.assertEquals(0,JPAService.getInstance().setRollBackMessages("user","from", 1));

  }

  @Test
  public void findAllMembersOfGroupTest() {

    User user = new User();
    user.setId(1);

    List<GroupMember> groupMembersList = new ArrayList<>();
    Group group = new Group();
    group.setId(2);
    group.setModeratorAuthRequired(true);

    GroupMember groupMember = new GroupMember();
    groupMember.setGroupUser(user);
    groupMembersList.add(groupMember);

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);


    // Find group Member by name
    when(criteriaBuilder.createQuery(Matchers.eq(GroupMember.class))).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.from(Matchers.eq(GroupMember.class))).thenReturn(groupMemberRoot);
    when(groupMemberCriteriaQuery.select(Matchers.<Root<GroupMember>>any())).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupMemberCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupMemberCriteriaQuery))).thenReturn(groupMemberQuery);
    when(groupMemberQuery.getResultList()).thenReturn(groupMembersList);


    Assert.assertEquals(1, JPAService.getInstance().findAllMembersOfGroup("str").size());

  }

  @Test
  public void updateModeratorStatusTest() {

    User user = new User();
    user.setId(1);

    Group group = new Group();
    group.setId(2);

    GroupMember groupMember = new GroupMember();
    groupMember.setGroupUser(user);



    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);



    //Find user by user name
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);


    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);

    // Find group Member by name
    when(criteriaBuilder.createQuery(Matchers.eq(GroupMember.class))).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.from(Matchers.eq(GroupMember.class))).thenReturn(groupMemberRoot);
    when(groupMemberCriteriaQuery.select(Matchers.<Root<GroupMember>>any())).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupMemberCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupMemberCriteriaQuery))).thenReturn(groupMemberQuery);
    when(groupMemberQuery.getSingleResult()).thenReturn(groupMember);
    Mockito.doNothing().when(session).update(groupMember);

    JPAService.getInstance().updateModeratorStatus("str","from", false);

  }


  @Test
  public void findNonMembersTest() {

    List<String> stringList = new ArrayList<>();
    stringList.add("atti");

    GroupMember groupMember = new GroupMember();

    User user = new User();

    List<GroupMember> groupMemberList = new ArrayList<>();

    Group group = new Group();
    group.setId(2);
    groupMemberList.add(groupMember);
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Find user by user name
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);

    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);


    // Find group Member by name
    when(criteriaBuilder.createQuery(Matchers.eq(GroupMember.class))).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.from(Matchers.eq(GroupMember.class))).thenReturn(groupMemberRoot);
    when(groupMemberCriteriaQuery.select(Matchers.<Root<GroupMember>>any())).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupMemberCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupMemberCriteriaQuery))).thenReturn(groupMemberQuery);
    when(groupMemberQuery.getResultList()).thenReturn(groupMemberList);


    Assert.assertEquals(0, JPAService.getInstance().findNonMembers(stringList, "gname").size());
  }


  @Test
  public void allGroupsForUserTest() {

    User user = new User();
    List<Group> groupList = new ArrayList<>();
    groupList.add(new Group());
    groupList.add(new Group());

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);


    //Find user by user name
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);
    when(query.getResultList()).thenReturn(groupList);


    Assert.assertEquals(0,JPAService.getInstance().allGroupsForUser("name", "gname").size());
  }

  @Test(expected = NullPointerException.class)
  public void allGroupsForUserTestNull() {

    User user = new User();
    List<Group> groupList = new ArrayList<>();
    groupList.add(new Group());
    groupList.add(new Group());

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);


    //Find user by user name
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(null);
    when(query.getResultList()).thenReturn(groupList);

    Assert.assertNull(JPAService.getInstance().allGroupsForUser("name", "gname").size());
  }

  @Test
  public void getAllGroupsForUserMapTest() {

    BigInteger userId = BigInteger.ONE;
    List<GroupMember> listGroupForUser = new ArrayList<>();


    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Get user from UserID
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(userId);
    when(query.executeUpdate()).thenReturn(1);
    when(nativeQuery.getResultList()).thenReturn(listGroupForUser);


    Assert.assertEquals(0,JPAService.getInstance().getAllGroupsForUser("name").size());

  }

  @Test
  public void getAllUsersForGroupNegativeTest() {

    Group group = new Group();

    List<GroupMember> groupMemberList = new ArrayList<>();


    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);
    when(nativeQuery.getResultList()).thenReturn(groupMemberList);


    Assert.assertEquals(0,JPAService.getInstance().getAllUsersForGroup("name").size());
  }

  @Test
  public void getAllUsersForGroupTest() {

    Group group = new Group();
    group.setId(1);

    GroupMember groupMember = new GroupMember();

    List<GroupMember> groupMemberList = new ArrayList<>();


    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);

    when(session.createNativeQuery(Matchers.any(), Matchers.<Class<GroupMember>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyInt())).thenReturn(query);
    when(nativeQuery.getResultList()).thenReturn(groupMemberList);

    Assert.assertEquals(0,JPAService.getInstance().getAllUsersForGroup("name").size());
  }


  @Test
  public void toggleAdminRightsTest() {
    BigInteger userId = BigInteger.ONE;

    Group group = new Group();
    group.setId(1);

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Get usrId from user name from UserID
    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);
    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<GroupMember>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.any())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(userId);
    when(query.executeUpdate()).thenReturn(1);


    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);

    Assert.assertTrue(JPAService.getInstance().toggleAdminRights("user", "group"));

  }


  @Test
  public void renameUpdateGroupTest() {
    Group group = new Group();
    group.setId(1);

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);


    Assert.assertFalse(JPAService.getInstance().renameUpdateGroup("gro", "group"));

    when(groupQuery.getSingleResult()).thenReturn(null);
    Assert.assertFalse(JPAService.getInstance().renameUpdateGroup("gro", "group"));
  }


  @Test
  public void userIsModeratorTest() {
    User user = new User();
    Group group = new Group();
    GroupMember groupMember = new GroupMember();

    List<GroupMember> groupMemberList = new ArrayList<>();
    groupMemberList.add(groupMember);

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Find user by user name
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);

    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);

    // Find group Member by name
    when(criteriaBuilder.createQuery(Matchers.eq(GroupMember.class))).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.from(Matchers.eq(GroupMember.class))).thenReturn(groupMemberRoot);
    when(groupMemberCriteriaQuery.select(Matchers.<Root<GroupMember>>any())).thenReturn(groupMemberCriteriaQuery);
    when(groupMemberCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupMemberCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupMemberCriteriaQuery))).thenReturn(groupMemberQuery);
    when(groupMemberQuery.getResultList()).thenReturn(groupMemberList);


    Assert.assertTrue(JPAService.getInstance().userIsModerator("gro", "group"));
  }

  @Test
  public void addFollowerTest() {

    User user = new User();

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Find user by user name
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);


    Assert.assertTrue(JPAService.getInstance().addFollower("gro", "group"));

    when(nativeQuery.getSingleResult()).thenReturn(null);
    Assert.assertFalse(JPAService.getInstance().addFollower("gro", "group"));
  }

  @Test
  public void getUnreadMessagesForGroupTest() {
    User user = new User();
    user.setName("atti");

    Group group = new Group();
    group.setId(1);
    Chat chat = new Chat();
    chat.setFromId(user);
    chat.setCreated(new Date());
    chat.setMsg("abc");
    chat.setIsGrpMsg(true);


    List<Chat> groupChat = new ArrayList<>();
    groupChat.add(chat);



    Map<String, String > dateMap = new HashMap<>();

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<Chat>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getResultList()).thenReturn(groupChat);


    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(Group.class))).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.from(Matchers.eq(Group.class))).thenReturn(groupRoot);
    when(groupCriteriaQuery.select(Matchers.<Root<Group>>any())).thenReturn(groupCriteriaQuery);
    when(groupCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(groupCriteriaQuery);
    when(session.createQuery(Matchers.eq(groupCriteriaQuery))).thenReturn(groupQuery);
    when(groupQuery.getSingleResult()).thenReturn(group);


    Assert.assertEquals(1,JPAService.getInstance().getUnreadMessagesForGroup("gro", dateMap).size());
  }

  @Test
  public void upgradeToSuperUserTest() {

    User user = new User();
    user.setId(1);

    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Find user by user name
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);

    when(session.createNativeQuery(Matchers.anyString())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyInt())).thenReturn(query);
    when(query.executeUpdate()).thenReturn(1);

    JPAService.getInstance().upgradeToSuperUser("atti");

  }

  @Test
  public void getAllFollowersTest() {
    User user = new User();

    UserFollow userFollow = new UserFollow();
    userFollow.setFollowedUser(user);

    List<UserFollow> userFollowList = new ArrayList<>();
    userFollowList.add(userFollow);


    when(sessionFactoryMock.openSession()).thenReturn(session);
    when(session.beginTransaction()).thenReturn(transaction);

    //Find user by user name
    when(session.createNativeQuery(Matchers.anyString(), Matchers.<Class<User>>any())).thenReturn(nativeQuery);
    when(query.setParameter(Matchers.anyInt(), Matchers.anyString())).thenReturn(query);
    when(nativeQuery.getSingleResult()).thenReturn(user);

    // Find Group Name
    when(session.getCriteriaBuilder()).thenReturn(criteriaBuilder);
    when(criteriaBuilder.createQuery(Matchers.eq(UserFollow.class))).thenReturn(userFollowCriteriaQuery);
    when(userFollowCriteriaQuery.from(Matchers.eq(UserFollow.class))).thenReturn(userFollowRoot);
    when(userFollowCriteriaQuery.select(Matchers.<Root<UserFollow>>any())).thenReturn(userFollowCriteriaQuery);
    when(userFollowCriteriaQuery.where(Matchers.<Expression<Boolean>>any())).thenReturn(userFollowCriteriaQuery);
    when(session.createQuery(Matchers.eq(userFollowCriteriaQuery))).thenReturn(userFollowQuery);
    when(userFollowQuery.getResultList()).thenReturn(userFollowList);

    Assert.assertEquals(1,JPAService.getInstance().getAllFollowers("gro").size());

    when(nativeQuery.getSingleResult()).thenReturn(null);
    Assert.assertEquals(0,JPAService.getInstance().getAllFollowers("gro").size());
  }


}
