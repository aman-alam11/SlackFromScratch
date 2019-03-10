package edu.northeastern.ccs.im.business.logic;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import edu.northeastern.ccs.im.model.LoginCredentials;
import edu.northeastern.ccs.im.model.User;

public class LoginHandler implements MessageHandler {
	
	private Gson gson;

	public LoginHandler() {
		gson = new Gson();
	}
	@Override
	public boolean handleMessage(String user, String message) {
		try {
			LoginCredentials lgn = gson.fromJson(message, LoginCredentials.class);
			System.out.println(lgn.getUserName());
			System.out.println(lgn.getPassword());
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
