package edu.northeastern.ccs.im.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Date;
import java.util.List;

public class JPAService {

	/**
	 * creating an object of UserDao to call the methods to perform operations on user.
	 */
	private UserDao ud;

	/**
	 * Creating an object of ChatDao to call the methods to perform operations on chat.
	 */
	private ChatDao cd;

	/**
	 * Initialize the SessionFactory instance.
	 */

	private static SessionFactory SESSION_FACTORY;

	static {
		// Create the SessionFactory using the ServiceRegistry
		SESSION_FACTORY = new Configuration().
				configure().
				addAnnotatedClass(User.class).
				addAnnotatedClass(Chat.class).
				buildSessionFactory();
	}

	/**
	 * Constructor to initialize userdao object.
	 */
	public JPAService(){
		ud = new UserDao(SESSION_FACTORY);
		cd = new ChatDao(SESSION_FACTORY);
	}

	public JPAService(SessionFactory sf){
		SESSION_FACTORY = sf;
		ud = new UserDao(SESSION_FACTORY);
		cd = new ChatDao(SESSION_FACTORY);
	}
	/**
	 * Create a new user.
	 * @param name
	 * @param password
	 */
	public boolean createUser(String name, String password) {
		return ud.create(name, null, password);
	}

	public void createUser(String name, String email, String password) {
		ud.create(name, email, password);
	}

	/**
	 * Read all users.
	 * @return
	 */
	public List<User> readAllUsers() {
		return ud.readAll();
	}

	/**
	 * Delete a user.
	 * @param id
	 */
	public void deleteUser(int id) {
		ud.delete(id);
	}

	/**
	 * Update a user.
	 * @param id
	 * @param name
	 * @param email
	 * @param password
	 */
	public void updateUser(int id, String name, String email, String password) {
		ud.update(id, name, email, password);
	}


	/**
	 * Find a user by his user name.
	 * @param name
	 * @return
	 */
	public User findUserByName(String name) {
    	return ud.findUserByName(name);
    }


	public String getHashFromUsername(String username) {
		return ud.findHashForUsername(username);
	}

	/**
	 * Close a session factory once the operations are done.
	 */
	public void closeSessionFactory() {
		SESSION_FACTORY.close();
	}

	/**
	 * Create a new message in chat.
	 * @param from_id
	 * @param to_id
	 * @param msg
	 * @param reply_to
	 * @param created
	 * @param expiery
	 * @param grpMsg
	 * @param isDelivered
	 */
	public void createChatMessage(int from_id, int to_id, String msg, int reply_to, Date created, Date expiery, Boolean grpMsg, Boolean isDelivered) {
		cd.create(from_id, to_id, msg, reply_to, created, expiery, grpMsg, isDelivered);
	}

	/**
	 * List all messages for a user or a group.
	 * @param receiver_id
	 * @return
	 */
	public List<Chat> findByReceiver(int receiver_id) {
		return cd.findByReceiver(receiver_id);
	}

	/**
	 * Delete the chat for a user or a group.
	 * @param receiver_id
	 */
	public void deleteChatByReceiver(int receiver_id) {
		cd.deleteChatByReceiver(receiver_id);
	}

	/**
	 * Delete a particular message.
	 * @param id
	 */
	public void deleteMessage(int id) {
		cd.delete(id);
	}
}
