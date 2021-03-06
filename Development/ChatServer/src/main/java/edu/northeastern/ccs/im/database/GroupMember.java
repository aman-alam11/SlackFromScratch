package edu.northeastern.ccs.im.database;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "group_member")
public class GroupMember implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User groupUser;

    @Id
    @ManyToOne
    @JoinColumn(name="group_id", nullable=false)
    private Group groupId;

    private boolean isModerator;

    @Column(name="creation_date", nullable=false)
    private Date creationDate;

    public GroupMember(){}

    public GroupMember(User groupUser, Group groupId, boolean isModerator) {
        this.groupUser = groupUser;
        this.groupId = groupId;
        this.isModerator = isModerator;
        this.creationDate = new Date();
    }

    public User getGroupUser() {
        return groupUser;
    }

    public void setGroupUser(User groupUser) {
        this.groupUser = groupUser;
    }

    public Group getGroupId() {
        return groupId;
    }

    public void setGroupId(Group groupId) {
        this.groupId = groupId;
    }

    public boolean isModerator() {
        return isModerator;
    }

    public void setModerator(boolean moderator) {
        isModerator = moderator;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
