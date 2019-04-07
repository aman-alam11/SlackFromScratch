package edu.northeastern.ccs.im.database;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "followers")
public class UserFollow implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User followedUser;

    @Id
    @ManyToOne
    @JoinColumn(name="follower_id", nullable=false)
    private User followerUser;

    @Column(name="follow_date", nullable=false)
    private Date followDate;

    public UserFollow(){}

    public UserFollow(User followedUser, User followerUser) {
        this.followedUser = followedUser;
        this.followerUser = followerUser;
        this.followDate = new Date();
    }

    public User getFollowedUser() {
        return followedUser;
    }

    public void setFollowedUser(User followedUser) {
        this.followedUser = followedUser;
    }

    public User getFollowerUser() {
        return followerUser;
    }

    public void setFollowerUser(User followerUser) {
        this.followerUser = followerUser;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }
}
