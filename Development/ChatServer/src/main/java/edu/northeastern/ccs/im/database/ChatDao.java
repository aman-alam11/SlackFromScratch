package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("all")
public class ChatDao {

	SessionFactory mSessionFactory;

    public ChatDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    /**
     * Create a new chat message.
     * @param fromId
     * @param toId
     * @param msg
     * @param replyTo
     * @param expiry
     * @param grpMsg
     * @param isDelivered
     */
    public int create(User fromId, User toId, String msg, int replyTo, Date expiry,
                   Boolean grpMsg, Boolean isDelivered) {
        // Create a session
        Session session = null;
        Transaction transaction = null;
        int id  = 0;
        try {
            session = mSessionFactory.openSession();
           // Begin a transaction
           transaction = session.beginTransaction();
           Chat chat = new Chat();
           chat.setFromId(fromId);
           chat.setToId(toId);
           chat.setMsg(msg);
           chat.setReplyTo(replyTo);
           chat.setCreated(new Date());
           chat.setExpiry(expiry);
           chat.setGrpMsg(grpMsg);
           chat.setIsDelivered(isDelivered);
           // Save the User
           id = (int) session.save(chat);
           // Commit the transaction
           transaction.commit();
        } catch (HibernateException ex) {
            // Print the Exception
            Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
            // If there are any exceptions, roll back the changes
            transaction.rollback();
        } finally {
           // Close the session
           session.close();
        }

     return id;
   }

   /**
    * Find chat for a particular user or a group.
    * @param receiverId
    * @return
    */

   public List<Chat> findByReceiver(int receiverId) {
	Session session = null;
	Transaction transaction = null;
	List<Chat> chat = null;
       try {
           session = mSessionFactory.openSession();
           // Begin a transaction
           transaction = session.beginTransaction();
           String sql = "FROM Chat C WHERE C.toId = :toId";

           Query query = session.createQuery(sql, Chat.class);
           query.setParameter("toId", receiverId);
           chat = query.getResultList();
           // Commit the transaction
           transaction.commit();
       } catch (Exception ex) {
           // Print the Exception
           Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
           // If there are any exceptions, roll back the changes
           transaction.rollback();
       } finally {
           // Close the session
           session.close();
       }
   	return chat;
   }

   /**
    * Delete the chat for a particular user or a group.
    * @param receiverId
    */
   public void deleteChatByReceiver(int receiverId) {
	   Session session = null;
       Transaction transaction = null;
       try {
           session = mSessionFactory.openSession();
           // Begin a transaction
           transaction = session.beginTransaction();
           String sql = "DELETE FROM Chat C WHERE C.toId = :toID";

	   	   	Query query = session.createQuery(sql, Chat.class);
	   	   	query.setParameter("toId", receiverId);
	   	   	query.executeUpdate();
           // Commit the transaction
           transaction.commit();
       } catch (Exception ex) {
           // Print the Exception
           Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
           // If there are any exceptions, roll back the changes
           transaction.rollback();
       } finally {
           // Close the session
           session.close();
       }
   }

   /**
    * Delete a particular message.
    * @param id
    */
   public void delete(int id) {
       // Create a session
       Session session = null;
       Transaction transaction = null;
       try {
           session = mSessionFactory.openSession();
           // Begin a transaction
           transaction = session.beginTransaction();
           // Get the User from the database.
           Chat chat = session.get(Chat.class, id);
           // Delete the User
           session.delete(chat);
           // Commit the transaction
           transaction.commit();
       } catch (Exception ex) {
           // Print the Exception
           Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
           // If there are any exceptions, roll back the changes
           transaction.rollback();
       } finally {
           // Close the session
           session.close();
       }
   }

	public void updateDeliveryStatus(int id, boolean status) {

		Transaction tx = null;
		try (Session session = mSessionFactory.openSession()) {
			tx = session.beginTransaction();
			Chat chat = (Chat) session.get(Chat.class, id);
			chat.setIsDelivered(status);
			session.update(chat);
			tx.commit();
		} catch (Exception e) {
            Logger.getLogger(this.getClass().getSimpleName()).info(e.getMessage());
            tx.rollback();
		}
	}
}
