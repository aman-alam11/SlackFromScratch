package edu.northeastern.ccs.im.database;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The User POJO. This class describes all the properties of a user. Such as email, password,
 * username etc.
 *
 * @author Mitresh
 */
@Entity
@Table(name = "users")
public class User implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", unique = true)
  private long id;

  @Column(name = "user_name", nullable = false, unique = true, length = 100)
  private String name;

  @Column(name = "user_email", nullable = false)
  private String email;

  @Column(name = "user_password", nullable = false)
  private String password;

  @Column(name = "is_super_user", nullable = false)
  private boolean isSuperUser;

  public User() {

  }

  public User(String username, String password, String email) {
    if (username == null || password == null) {
      throw new IllegalArgumentException("Username and Password can't be null");
    }
    this.name = username;
    this.password = password;

    this.email = (email != null) ? email : "";
  }

  /**
   * This method returns the id of a user.
   *
   * @return an integer id.
   */
  protected long getId() {
    return id;
  }

  /**
   * Set the user id of a current user object.
   *
   * @param id Takes an integer id to set it as an id of a user.
   */
  protected void setId(long id) {
    this.id = id;
  }

  /**
   * This method returns the user name of a current user.
   *
   * @return a String username.
   */
  public String getName() {
    return name;
  }

  /**
   * This method sets the user name of a user.
   *
   * @param name Takes a string name to set it as the username for the user.
   */
  protected void setName(String name) {
    this.name = name;
  }

  /**
   * This method sets the email of current user.
   *
   * @return a string email.
   */
  protected String getEmail() {
    if (email != null) {
      return email;
    }

    return "";
  }

  /**
   * This method sets the email of a user.
   *
   * @param email Takes a String email to set it as the email for the user.
   */
  protected void setEmail(String email) {
    this.email = (email == null) ? "" : email;
  }

  /**
   * The method returns the password of a user.
   *
   * @return The string password of a user.
   */
  protected String getPassword() {
    return password;
  }

  /**
   * Thei method sets the password of a user.
   *
   * @param password takes a string password to set it as the password for the user.
   */
  protected void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return id + "\t" + name;
  }

  public boolean isSuperUser() {
    return isSuperUser;
  }

}
