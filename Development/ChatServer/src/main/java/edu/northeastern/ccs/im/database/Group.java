package edu.northeastern.ccs.im.database;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "groups")
public class Group implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id", unique = true)
    private int id;

    @Column(name = "group_name", unique = true)
    private String gName;

    @ManyToOne
    @JoinColumn(name="created_by", nullable=false)
    private User gCreator;

    public Group(){}

    public Group(String gName, User gCreator) {
        this.gName = gName;
        this.gCreator = gCreator;
    }

    protected int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected String getgName() {
        return gName;
    }

    protected void setgName(String gName) {
        this.gName = gName;
    }

    protected User getgCreator() {
        return gCreator;
    }

    protected void setgCreator(User gCreator) {
        this.gCreator = gCreator;
    }
}
