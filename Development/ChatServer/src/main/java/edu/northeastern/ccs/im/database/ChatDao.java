package edu.northeastern.ccs.im.database;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import edu.northeastern.ccs.im.model.ChatModel;

public class ChatDao {

	SessionFactory mSessionFactory;

    public ChatDao(SessionFactory sf){
        mSessionFactory = sf;
    }

    /**
     * Create a new chat message.
     * @param fromId
     * @param toId
     * @param chatModel
     */
    public long create(User fromId, User toId, ChatModel chatModel) {
		// Create a session
		Session session = mSessionFactory.openSession();
		Group  group = null;
		Transaction transaction = null;
        transaction = session.beginTransaction();
		long returnId = 0;
		try {
			Chat chat = new Chat();
			chat.setFromId(fromId);
			chat.setToId(toId);
			chat.setMsg(chatModel.getMsg());
			if (chatModel.getGroupName() != null && chatModel.getGroupName().length() > 0) {
				group = JPAService.getInstance().findGroupByName(chatModel.getGroupName());
			}
      chat.setGroupId(group);
			chat.setCreated(new Date());
			chat.setExpiry(chatModel.getExpiry());
			chat.setIsGrpMsg(chatModel.getGroupName() != null && chatModel.getGroupName().length() > 0);
			chat.setIsDelivered(chatModel.getDelivered());
			// Save the User
      session.save(chat);
			returnId = chat.getId();

			// Commit the transaction
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
		return returnId;
	}
   
   /**
    * Find chat for a particular user or a group.
    * @param receiverId
    * @return
    */
   public List<Chat> findByReceiver(long receiverId) {
	Session session = null;
	Transaction transaction = null;
	session = mSessionFactory.openSession();
	// Begin a transaction
    transaction = session.beginTransaction();
	List<Chat> chat = null;
       try {
           String sql = "select *from chat where chat.To_id = ?";

           Query query = session.createNativeQuery(sql, Chat.class);
           query.setParameter(1, receiverId);
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
   public void deleteChatByReceiver(long receiverId) {
	   Session session = null;
       Transaction transaction = null;

       session = mSessionFactory.openSession();
       // Begin a transaction
       transaction = session.beginTransaction();
       try {
           String sql = "delete from chat where chat.To_id = ?";

	   	   	Query query = session.createNativeQuery(sql, Chat.class);
	   	   	query.setParameter(1, receiverId);
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
   public void delete(long id) {
       // Create a session
       Session session = null;
       Transaction transaction = null;

       session = mSessionFactory.openSession();
       // Begin a transaction
       transaction = session.beginTransaction();
       try {
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

    /**
     * This method updtes the delivery status of a message from unread to read once the user logs in and
     * check the unread messages.
     * @param id The long id of a message in chat table.
     * @param status Boolean status
     * @return
     */
	public boolean updateDeliveryStatus(long id, boolean status) {
        Session session = mSessionFactory.openSession();
		Transaction tx = null;
        tx = session.beginTransaction();
		try {
			Chat chat = session.get(Chat.class, id);
			chat.setIsDelivered(status);
			 session.update(chat);
			tx.commit();
            return true;
		} catch (Exception e) {
            Logger.getLogger(this.getClass().getSimpleName()).info(e.getMessage());
            tx.rollback();
            return false;
		} finally {
		    //Close the session.
		    session.close();
        }
	}
}
