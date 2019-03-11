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

	// Create the SessionFactory using the ServiceRegistry
	SessionFactory mSessionFactory = new Configuration().
			configure().
			addAnnotatedClass(User.class).
			addAnnotatedClass(Chat.class).
			buildSessionFactory();

	/**
	 * Constructor to initialize userdao object.
	 */
	public JPAService(){
		ud = new UserDao(mSessionFactory);
		cd = new ChatDao(mSessionFactory);
	}

	public JPAService(SessionFactory sf){
		mSessionFactory = sf;
		ud = new UserDao(mSessionFactory);
		cd = new ChatDao(mSessionFactory);
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
		mSessionFactory.close();
	}

	/**
	 * Create a new message in chat.
	 * @param fromId
	 * @param toId
	 * @param msg
	 * @param replyTo
	 * @param expiry
	 * @param grpMsg
	 * @param isDelivered
	 */
	public void createChatMessage(int fromId, int toId, String msg, int replyTo,
																Date expiry, Boolean grpMsg, Boolean isDelivered) {
		cd.create(fromId, toId, msg, replyTo, expiry, grpMsg, isDelivered);
	}

	/**
	 * List all messages for a user or a group.
	 * @param receiverId
	 * @return
	 */
	public List<Chat> findByReceiver(int receiverId) {
		return cd.findByReceiver(receiverId);
	}

	/**
	 * Delete the chat for a user or a group.
	 * @param receiverId
	 */
	public void deleteChatByReceiver(int receiverId) {
		cd.deleteChatByReceiver(receiverId);
	}

	/**
	 * Delete a particular message.
	 * @param id
	 */
	public void deleteMessage(int id) {
		cd.delete(id);
	}
}
