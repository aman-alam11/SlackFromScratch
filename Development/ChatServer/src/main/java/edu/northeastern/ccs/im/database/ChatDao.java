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

	private static SessionFactory mSessionFactory;

    public ChatDao(SessionFactory sf){
        mSessionFactory = sf;
    }
   /**
    * Create a new chat message.
    * @param from_id
    * @param to_id
    * @param msg
    * @param reply_to
    * @param expiery
    * @param grpMsg
    * @param isDelivered
    */
   public boolean create(int from_id, int to_id, String msg, int reply_to, Date created, Date expiery,
                         Boolean grpMsg, Boolean isDelivered) {

     // Create a session
     Session session = mSessionFactory.openSession();
     Transaction transaction = null;
     boolean isTransactionSuccessful = false;
     try {
       // Begin a transaction
       transaction = session.beginTransaction();
       Chat chat = new Chat();
       chat.setFrom_id(from_id);
       chat.setTo_id(to_id);
       chat.setMsg(msg);
       chat.setReply_to(reply_to);
       chat.setCreated(created);
       chat.setExpiery(expiery);
       chat.setGrpMsg(grpMsg);
       chat.setIsDelivered(isDelivered);
       // Save the User
       session.save(chat);
       // Commit the transaction
       transaction.commit();
       isTransactionSuccessful = true;
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
    * @param receiver_id
    * @return
    */
	public List<Chat> findByReceiver(int receiver_id) {
		try (Session session = mSessionFactory.openSession()) {
			String sql = "select *from chat where chat.To_id = ?";

			Query query = session.createNativeQuery(sql, Chat.class);
			query.setParameter(1, receiver_id);
			List<Chat> chat = query.getResultList();

			return chat;
		}
	}

   /**
    * Delete the chat for a particular user or a group.
    * @param receiver_id
    */
   public void deleteChatByReceiver(int receiver_id) {
	   Session session = mSessionFactory.openSession();
       Transaction transaction = null;
       try {
           // Begin a transaction
           transaction = session.beginTransaction();
           String sql = "delete from chat where chat.To_id = ?";

	   	   	Query query = session.createNativeQuery(sql, Chat.class);
	   	   	query.setParameter(1, receiver_id);
	   	   	query.executeUpdate();
           // Commit the transaction
           transaction.commit();
       } catch (HibernateException ex) {
           // If there are any exceptions, roll back the changes
           if (transaction != null) {
               transaction.rollback();
           }
           // Print the Exception
           ex.printStackTrace();
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
   
	public void updateDeliveryStatus(String id, boolean status) {

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
