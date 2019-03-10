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
	
	@Column(name="From_user_id", nullable=false)
	private int from_id;
	
	@Column(name="To_id",nullable=false)
	private int to_id;
	
	@Column(name="Message", nullable=false)
	private String msg;
	
	@Column(name="ReplyTo", nullable=true)
	private int reply_to;
	
	@Column(name="Creation_date", nullable=false)
	private Date created;
	
	@Column(name="Expiery_time", nullable=true)
	private Date expiery;
	
	@Column(name="isGrpMsg", nullable=false)
	private Boolean grpMsg;
	
	@Column(name="isDelivered", nullable=false)
	private Boolean isDelivered;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFrom_id() {
		return from_id;
	}

	public void setFrom_id(int from_id) {
		this.from_id = from_id;
	}

	public int getTo_id() {
		return to_id;
	}

	public void setTo_id(int to_id) {
		this.to_id = to_id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getReply_to() {
		return reply_to;
	}

	public void setReply_to(int reply_to) {
		this.reply_to = reply_to;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getExpiery() {
		return expiery;
	}

	public void setExpiery(Date expiery) {
		this.expiery = expiery;
	}

	public Boolean getGrpMsg() {
		return grpMsg;
	}

	public void setGrpMsg(Boolean grpMsg) {
		this.grpMsg = grpMsg;
	}

	public Boolean getIsDelivered() {
		return isDelivered;
	}

	public void setIsDelivered(Boolean isDelivered) {
		this.isDelivered = isDelivered;
	}
	
	
}
