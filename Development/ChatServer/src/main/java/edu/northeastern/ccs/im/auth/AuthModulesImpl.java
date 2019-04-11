package edu.northeastern.ccs.im.auth;

import org.springframework.security.crypto.bcrypt.BCrypt;

import edu.northeastern.ccs.im.database.JPAService;

/**
 * This is the implementation class of AuthModles.
 */
public class AuthModulesImpl implements AuthModules {

  @Override
  public boolean createAccount(String username, String password, JPAService mJpaService) {
    // Save the username and hash

      String hashedPwd = BCrypt.hashpw(password, BCrypt.gensalt());

      return mJpaService.createUser(username, hashedPwd);
  }

  @Override
  public boolean loginIn(String username, String password, JPAService mJpaService) {
    // Check if the username exists
    if (mJpaService.findUserByName(username) == null) {
      return false;
    }

    // Get the hash for the username from user and verify using BCrypt
    String hash = mJpaService.getHashFromUsername(username);
    if (hash != null) {
      return BCrypt.checkpw(password, hash);
    }
    return false;
  }

}
