package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

/**
 * The MessageService class provides high-level methods for managing messages.
 * It interacts with the MessageDAO to perform CRUD operations on messages.
 */
public class MessageService {

    MessageDAO messageDAO;

    /**
     * Constructs a new MessageService and initializes the associated MessageDAO.
     */
    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * Creates a new message and stores it in the database.
     *
     * @param message The Message object to be created.
     * @return The created Message object, or null if the message content or posted_by is invalid.
     */
    public Message createMessage(Message message) {
        Message createdMessage = messageDAO.createMessage(message);
        
        // Check if the message text is not blank, not too long, and posted_by is valid
        if (!message.getMessage_text().isBlank() && message.getMessage_text().length() < 255 
            && message.getPosted_by() != 0) {
            return createdMessage;
        }
        return null;
    }

    /**
     * Retrieves all messages from the database.
     *
     * @return A list of Message objects containing all messages.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Retrieves a message by its unique message_id from the database.
     *
     * @param messageId The unique identifier of the message to retrieve.
     * @return The Message object with the specified message_id, or null if not found.
     */
    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    /**
     * Deletes a message by its unique message_id from the database.
     *
     * @param messageId The unique identifier of the message to delete.
     * @return The Message object that was deleted, or null if the message does not exist.
     */
    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    /**
     * Updates the message text of a message with the given message_id in the database.
     *
     * @param messageId       The unique identifier of the message to update.
     * @param newMessageText  The new message text to set for the message.
     * @return The updated Message object, or null if the new message_text is invalid.
     */
    public Message updateMessageText(int messageId, String newMessageText) {
        // Check if the new message_text is not blank and not over 255 characters
        if (newMessageText != null && !newMessageText.isEmpty() && newMessageText.length() < 255) {
            return messageDAO.updateMessageText(messageId, newMessageText);
        } else {
            return null; // Invalid new message_text
        }
    }

    /**
     * Retrieves messages posted by a user with the given account_id from the database.
     *
     * @param account_id The unique identifier of the user.
     * @return A list of Message objects posted by the user, or an empty list if none are found.
     */
    public List<Message> getMessageByUser(int account_id) {
        return messageDAO.getMessagesByUserId(account_id);
    }
}
