package edu.northeastern.ccs.im.database;

import java.io.Serializable;

import javax.persistence.*;

/**
 * The User POJO. This class describes all the properties of a user. 
 * Such as email, password, username etc. 
 * @author Mitresh
 *
 */
@Entity
@Table(name = "users")
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", unique = true)
    private int id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String name;

    @Column(name = "user_email", nullable = false)
    private String email;
    
    @Column(name = "user_password", nullable = false)
    private String password;

    /**
     * This method returns the id of a user.
     * @return an integer id.
     */
	public int getId() {
		return id;
	}

	/**
	 * Set the user id of a current user object. 
	 * @param id Takes an integer id to set it as an id of a user.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * This method returns the user name of a current user.
	 * @return a String username.
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method sets the user name of a user.
	 * @param name Takes a string name to set it as the username for the user.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This method sets the email of current user.
	 * @return a string email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * This method sets the email of a user.
	 * @param email Takes a String email to set it as the email for the user.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * The method returns the password of a user.
	 * @return The string password of a user.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Thei method sets the password of a user.
	 * @param password takes a string password to set it as the password for the user.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
    
	@Override
    public String toString() {
        return id + "\t" + name;
    }
}
