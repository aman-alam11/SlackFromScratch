package edu.northeastern.ccs.im.database;

import java.util.List;

import org.hibernate.Session;

public class JPAService {

	/**
	 * creating an object of UserDao to call the methods to perform operations on user.
	 */
	private UserDao ud;
	
	/**
	 * Constructor to initialize userdao object.
	 */
	JPAService(){
		ud = new UserDao();
	}
	
	/**
	 * Create a new user.
	 * @param id
	 * @param name
	 * @param email
	 * @param password
	 */
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
	
	/**
	 * Close a session factory once the operations are done.
	 */
	public void closeSessionFactory() {
		ud.closeSessionFactory();
	}
}
