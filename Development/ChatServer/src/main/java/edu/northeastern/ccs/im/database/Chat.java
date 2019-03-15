package edu.northeastern.ccs.im.database;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * The Chat POJO. This class describes all the properties of a chat. Such as sender, receiver,
 * message etc.
 *
 * @author Mitresh
 */
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

	/**
	 * This method returns the id of a chat object.
	 *
	 * @return
	 */
	protected int getId() {
		return id;
	}

	/**
	 * This method sets the id of the current chat object.
	 *
	 * @param id Takes an integer id to set it as an id of a chat.
	 */
	protected void setId(int id) {
		this.id = id;
	}

	/**
	 * This method returns the id of a sender.
	 *
	 * @return
	 */
	protected User getFromId() {
		return fromId;
	}

	/**
	 * This method sets the id of a sender for current chat object.
	 *
	 * @param fromId Takes an integer id to set it as sender id.
	 */
	protected void setFromId(User fromId) {
		this.fromId = fromId;
	}

	/**
	 * This method returns the id of a receiver.
	 *
	 * @return
	 */
	protected User getToId() {
		return toId;
	}

	/**
	 * This method sets the receiver id for the current chat object.
	 *
	 * @param toId Takes the integer id to set it as a receiver id.
	 */
	protected void setToId(User toId) {
		this.toId = toId;
	}

	/**
	 * This method returns the message that is sent between the users for that chat.
	 *
	 * @return
	 */
	protected String getMsg() {
		return msg;
	}

	/**
	 * This method sets the message for current chat object.
	 *
	 * @param msg Takes string message and set it as a message for the object.
	 */
	protected void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * This method returns the id if the message is a reply to particular message in group.
	 * @return
	 */
	protected int getReplyTo() {
		return replyTo;
	}

	/**
	 * This method sets the id of a chat to which a reply is added.
	 *
	 * @param replyTo Takes the integer id of to set it as a reply to.
	 */
	protected void setReplyTo(int replyTo) {
		this.replyTo = replyTo;
	}

	/**
	 * This method returns the time when the chat message was sent.
	 *
	 * @return
	 */
	protected Date getCreated() {
		return created;
	}

	/**
	 * This method sets the date and time at which the object is created.
	 *
	 * @param created Date at which the object was created.
	 */
	protected void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * This method returns the time at which chat will be removed.
	 *
	 * @return
	 */
	protected Date getExpiry() {
		return expiry;
	}

	/**
	 * This method sets the time at which a chat will be removed.
	 *
	 * @param expiry Date at which the message will be auto deleted.
	 */
	protected void setExpiry(Date expiry) {
		this.expiry = expiry;
	}

	/**
	 * This method returns if particular chat is a group message or not.
	 *
	 * @return
	 */
	protected Boolean getGrpMsg() {
		return grpMsg;
	}

	/**
	 * This method takes the boolean flag and sets if a message is group or not.
	 *
	 * @param grpMsg Boolean which will determine if the message is a group message or not.
	 */
	protected void setGrpMsg(Boolean grpMsg) {
		this.grpMsg = grpMsg;
	}

	/**
	 * This method returns if the message is delivered to the receiver or not.
	 *
	 * @return
	 */
	protected Boolean getIsDelivered() {
		return isDelivered;
	}

	/**
	 * This method sets if a message is delivered or not.
	 *
	 * @param isDelivered Boolean which determines if the message is delivered or not.
	 */
	protected void setIsDelivered(Boolean isDelivered) {
		this.isDelivered = isDelivered;
	}

}
