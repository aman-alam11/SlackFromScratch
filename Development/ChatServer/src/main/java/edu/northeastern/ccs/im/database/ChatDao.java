package edu.northeastern.ccs.im.database;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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
    public boolean create(int fromId, int toId, String msg, int replyTo, Date expiry,
                   Boolean grpMsg, Boolean isDelivered) {
        // Create a session
        Session session = mSessionFactory.openSession();
        Transaction transaction = null;
        boolean isTransactionSuccessful = false;
        try {
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
           session.save(chat);
           // Commit the transaction
           transaction.commit();
        } catch (HibernateException ex) {
           // If there are any exceptions, roll back the changes
           if (transaction != null) {
               transaction.rollback();
           }
           // Print the Exception
            Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
        } finally {
           // Close the session
           session.close();
        }

     return isTransactionSuccessful;
   }

   /**
    * Find chat for a particular user or a group.
    * @param receiverId
    * @return
    */

   public List<Chat> findByReceiver(int receiverId) {
	Session session = mSessionFactory.openSession();
	Transaction transaction = null;
	List<Chat> chat = null;
       try {
           // Begin a transaction
           transaction = session.beginTransaction();
           String sql = "select *from chat where chat.To_id = ?";

           Query query = session.createNativeQuery(sql, Chat.class);
           query.setParameter(1, receiverId);
           chat = query.getResultList();
           // Commit the transaction
           transaction.commit();
       } catch (HibernateException ex) {
           // If there are any exceptions, roll back the changes
           if (transaction != null) {
               transaction.rollback();
           }
           // Print the Exception
           Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
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
	   Session session = mSessionFactory.openSession();
       Transaction transaction = null;
       try {
           // Begin a transaction
           transaction = session.beginTransaction();
           String sql = "delete from chat where chat.To_id = ?";

	   	   	Query query = session.createNativeQuery(sql, Chat.class);
	   	   	query.setParameter(1, receiverId);
	   	   	query.executeUpdate();
           // Commit the transaction
           transaction.commit();
       } catch (HibernateException ex) {
           // If there are any exceptions, roll back the changes
           if (transaction != null) {
               transaction.rollback();
           }
           // Print the Exception
           Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
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
       Session session = mSessionFactory.openSession();
       Transaction transaction = null;
       try {
           // Begin a transaction
           transaction = session.beginTransaction();
           // Get the User from the database.
           Chat chat = session.get(Chat.class, id);
           // Delete the User
           session.delete(chat);
           // Commit the transaction
           transaction.commit();
       } catch (HibernateException ex) {
           // If there are any exceptions, roll back the changes
           if (transaction != null) {
               transaction.rollback();
           }
           // Print the Exception
         Logger.getLogger(this.getClass().getSimpleName()).info(ex.getMessage());
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
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		}
	}

   /**
    * Close the session factory.
    */
   public void closeSessionFactory() {
   	mSessionFactory.close();
   }
}
