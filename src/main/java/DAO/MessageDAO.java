package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    /**
     * Creates a new message in the database.
     *
     * @param message The Message object to be created.
     * @return The Message object with its generated message_id, or null if creation fails.
     */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            // SQL query to insert a new message
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by()); // Assuming postedBy is the account_id
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating message failed, no rows affected.");
            }

            ResultSet keys = preparedStatement.getGeneratedKeys();
            keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                int messageId = keys.getInt(1);
                message.setMessage_id(messageId);
                return message;
            } else {
                throw new SQLException("Creating message failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating message: " + e.getMessage());
        }
        return null;
    }

    /**
     * Retrieves all messages from the database.
     *
     * @return A list of Message objects containing all messages.
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            // SQL query to select all messages
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                messages.add(message);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    /**
     * Retrieves a message from the database by its unique message_id.
     *
     * @param messageId The unique identifier of the message to retrieve.
     * @return The Message object with the specified message_id, or null if not found.
     */
    public Message getMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                return message;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    /**
     * Deletes a message from the database by its unique message_id.
     *
     * @param messageId The unique identifier of the message to delete.
     * @return The Message object that was deleted, or null if the message does not exist.
     */
    public Message deleteMessageById(int messageId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String selectSql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, messageId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve the message details before deletion
                int deletedMessageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                // Delete the message
                String deleteSql = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, messageId);
                int deletedRows = deleteStatement.executeUpdate();

                if (deletedRows > 0) {
                    // Return the deleted message
                    return new Message(deletedMessageId, postedBy, messageText, timePostedEpoch);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Updates the message text of a message with the given message_id in the database.
     *
     * @param messageId The unique identifier of the message to update.
     * @param newMessageText The new message text to set for the message.
     * @return The Message object with the updated message text, or null if the update fails or the message is not found.
     */
    public Message updateMessageText(int messageId, String newMessageText) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Create a prepared statement with the SQL UPDATE statement
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set the new message_text and message_id in the prepared statement
            preparedStatement.setString(1, newMessageText);
            preparedStatement.setInt(2, messageId);

            // Execute the update
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // If the update was successful, fetch the updated message
                String selectSql = "SELECT * FROM Message WHERE message_id = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                selectStatement.setInt(1, messageId);
                ResultSet resultSet = selectStatement.executeQuery();

                if (resultSet.next()) {
                    int updatedMessageId = resultSet.getInt("message_id");
                    int postedBy = resultSet.getInt("posted_by");
                    String updatedMessageText = resultSet.getString("message_text");
                    long timePostedEpoch = resultSet.getLong("time_posted_epoch");

                    // Create a Message object with the updated values
                    Message updatedMessage = new Message(updatedMessageId, postedBy, updatedMessageText, timePostedEpoch);
                    return updatedMessage;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Update failed or message not found
    }

    /**
     * Retrieves a list of messages posted by a user with the given account_id.
     *
     * @param account_id The unique identifier of the user.
     * @return A list of Message objects posted by the user, or an empty list if none are found.
     */
    public List<Message> getMessagesByUserId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();

        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );

                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }
    
}
