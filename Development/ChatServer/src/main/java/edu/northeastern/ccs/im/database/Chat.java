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
    private long id;

  @ManyToOne
	@JoinColumn(name="From_user_id", nullable=false)
	private User fromId;

  @ManyToOne
	@JoinColumn(name="To_id", nullable=false)
	private User toId;
	
	@Column(name="Message", nullable=false)
	private String msg;
	
	@Column(name="ReplyTo", nullable=true)
	private long replyTo;
	
	@Column(name="Creation_date", nullable=false)
	private Date created;
	
	@Column(name="Expiry_time", nullable=true)
	private Date expiry;
	
	@Column(name="isGrpMsg", nullable=false)
	private Boolean isGrpMsg;
	
	@Column(name="isDelivered", nullable=false)
	private Boolean isDelivered;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getFromId() {
		return fromId;
	}

	public void setFromId(User fromId) {
		this.fromId = fromId;
	}

	public User getToId() {
		return toId;
	}

	public void setToId(User toId) {
		this.toId = toId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public long getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(long replyTo) {
		this.replyTo = replyTo;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getExpiry() {
		return expiry;
	}

	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	public Boolean getIsDelivered() {
		return isDelivered;
	}

	public void setIsDelivered(Boolean isDelivered) {
		this.isDelivered = isDelivered;
	}
	
	public Boolean getIsGrpMsg() {
		return isGrpMsg;
	}

	public void setIsGrpMsg(Boolean isGrpMsg) {
		this.isGrpMsg = isGrpMsg;
	}
	
	
}
