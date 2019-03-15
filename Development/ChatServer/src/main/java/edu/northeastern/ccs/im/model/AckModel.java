package edu.northeastern.ccs.im.model;

public class AckModel {

    private String errorMessage;
    private boolean isUserAuthenticated;
    private boolean isLogin;
    private String mUsername;

    public AckModel(boolean isUserAuth, String errorMsg, boolean isLogin) {
        this.isUserAuthenticated = isUserAuth;
        this.errorMessage = errorMsg;
        this.isLogin = isLogin;
    }

    public AckModel(boolean isUserAuth, String errorMsg, boolean isLogin, String username) {
        this.isUserAuthenticated = isUserAuth;
        this.errorMessage = errorMsg;
        this.isLogin = isLogin;
        this.mUsername = (username == null) ? "" : username;
    }

    public boolean isUserAuthenticated() {
        return isUserAuthenticated;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    public boolean isLogin() {
        return isLogin;
    }

    public String getUsername() {
        return mUsername;
    }
}