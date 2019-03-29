package edu.northeastern.ccs.im.database;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "groups")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", unique = true)
    private long id;

    @Column(name = "group_name", unique = true)
    private String gName;

    @ManyToOne
    @JoinColumn(name="created_by", nullable=false)
    private User gCreator;
    
    @Column(name="creation_date", nullable=false)
    private Date creationDate;
    
    @Column(name="is_auth_req", nullable=false)
    private boolean isAuthRequired;

		public Group(){}

    public Group(String gName, User gCreator, boolean isAuthReq) {
        this.gName = gName;
        this.gCreator = gCreator;
        this.creationDate = new Date();
        this.isAuthRequired = isAuthReq;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public User getgCreator() {
        return gCreator;
    }

    public void setgCreator(User gCreator) {
        this.gCreator = gCreator;
    }
    
    public Date getCreationDate() {
			return creationDate;
		}

		public void setCreationDate(Date creationDate) {
			this.creationDate = creationDate;
		}
		
		public boolean isModeratorAuthRequired() {
			return isAuthRequired;
		}

		public void setModeratorAuthRequired(boolean isModeratorAuthRequired) {
			this.isAuthRequired = isModeratorAuthRequired;
		}
}
