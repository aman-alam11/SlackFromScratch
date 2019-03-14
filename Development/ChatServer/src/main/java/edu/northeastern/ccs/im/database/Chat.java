package edu.northeastern.ccs.im.database;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="chat")
public class Chat implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", unique = true)
    private int id;

    @ManyToOne
	@JoinColumn(name="From_user_id", nullable=false)
	private User fromId;

    @ManyToOne
	@JoinColumn(name="To_id",nullable=false)
	private User toId;
	
	@Column(name="Message", nullable=false)
	private String msg;
	
	@Column(name="ReplyTo", nullable=true)
	private int replyTo;
	
	@Column(name="Creation_date", nullable=false)
	private Date created;
	
	@Column(name="Expiry_time", nullable=true)
	private Date expiry;
	
	@Column(name="isGrpMsg", nullable=false)
	private Boolean grpMsg;
	
	@Column(name="isDelivered", nullable=false)
	private Boolean isDelivered;

	protected int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	protected User getFromId() {
		return fromId;
	}

	protected void setFromId(User fromId) {
		this.fromId = fromId;
	}

	protected User getToId() {
		return toId;
	}

	protected void setToId(User toId) {
		this.toId = toId;
	}

	protected String getMsg() {
		return msg;
	}

	protected void setMsg(String msg) {
		this.msg = msg;
	}

	protected int getReplyTo() {
		return replyTo;
	}

	protected void setReplyTo(int replyTo) {
		this.replyTo = replyTo;
	}

	protected Date getCreated() {
		return created;
	}

	protected void setCreated(Date created) {
		this.created = created;
	}

	protected Date getExpiry() {
		return expiry;
	}

	protected void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	protected Boolean getGrpMsg() {
		return grpMsg;
	}

	protected void setGrpMsg(Boolean grpMsg) {
		this.grpMsg = grpMsg;
	}

	protected Boolean getIsDelivered() {
		return isDelivered;
	}

	protected void setIsDelivered(Boolean isDelivered) {
		this.isDelivered = isDelivered;
	}

}
