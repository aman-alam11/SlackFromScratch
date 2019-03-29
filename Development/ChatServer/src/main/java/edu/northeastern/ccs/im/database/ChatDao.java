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
    public long create(User fromId, User toId, ChatModel chatModel) {
		// Create a session
		Session session = mSessionFactory.openSession();
		Group  group = null;
		Transaction transaction = null;
		long returnId = 0;
		try {
			// Begin a transaction
			transaction = session.beginTransaction();
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
	List<Chat> chat = null;
       try {
           session = mSessionFactory.openSession();
           // Begin a transaction
           transaction = session.beginTransaction();
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
       try {
           session = mSessionFactory.openSession();
           // Begin a transaction
           transaction = session.beginTransaction();
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

	public boolean updateDeliveryStatus(long id, boolean status) {

		Transaction tx = null;
		try (Session session = mSessionFactory.openSession()) {
			tx = session.beginTransaction();
			Chat chat = (Chat) session.get(Chat.class, id);
			chat.setIsDelivered(status);
			 session.update(chat);
			tx.commit();
      return true;
		} catch (Exception e) {
            Logger.getLogger(this.getClass().getSimpleName()).info(e.getMessage());
            tx.rollback();
            return false;
		}
	}
}
