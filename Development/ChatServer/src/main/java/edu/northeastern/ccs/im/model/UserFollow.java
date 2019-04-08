package edu.northeastern.ccs.im.model;

public class UserFollow {

    private String userName;

    private String followerName;

    public UserFollow(String userName, String followerName) {
        this.userName = userName;
        this.followerName = followerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }
}
