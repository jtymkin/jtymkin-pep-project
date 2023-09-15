package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

public class SocialMediaDAO {

    public List<Account> getAllAccounts()
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Account> accounts = new ArrayList<>();
        try{

            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM account";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), 
                    rs.getString("password"));

                accounts.add(account);

            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return accounts;
        
    }

    public Account addAccount(Account account) {
        Connection connection = null;
        try {
            // sql
            connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO account(username, password) VALUES (?,?)";

            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());

            // execute statement
            statement.executeUpdate();

            // process
            // get the keys
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return new Account(keys.getInt(1), account.getUsername(), account.getPassword());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public Account getAccountById(int account_id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "SELECT * FROM account WHERE acount_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setInt method here.
            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account verifyUserCredentials(Account account) {
        System.out.println("Account Object - Username: " + account.getUsername());
        System.out.println("Account Object - Password: " + account.getPassword());
        System.out.println("Account Object - Account ID: " + account.getAccount_id());
    
        Connection connection = ConnectionUtil.getConnection();
        try {
            // Write SQL logic here
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
    
            // Write preparedStatement's setString method here
            preparedStatement.setString(1, account.getUsername());
    
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
    
                String storedPassword = rs.getString("password");
                int accountId = rs.getInt("account_id");
                System.out.println("Database - Username: " + account.getUsername());
                System.out.println("Database - Stored Password: " + storedPassword);
                System.out.println("Database - Account ID: " + accountId);
    
                if (account.getPassword().equals(storedPassword)) {
                    // Update the accountId of the Account object
                    account.setAccount_id(accountId);
                    return account;
                }
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message createMessage(Message message) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by()); // Assuming postedBy is the account_id
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating message failed, no rows affected.");
            }

            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int messageId = generatedKeys.getInt(1);
                message.setMessage_id(messageId);
                return message;
            } else {
                throw new SQLException("Creating message failed, no ID obtained.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error creating message: " + e.getMessage());
            return null;
        } finally {
            // Close resources in a finally block
            // (omitting for brevity, but ensure you close resources properly)
        }
    }



    public List<Message> getAllMessages()
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
        try{

            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message";
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                messages.add(message);

            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return messages;
        
    }

    public Message getMessageById(int messageId) 
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            rs = preparedStatement.executeQuery();

            while(rs.next())
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                return message;

            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
        
    }

    public Message deleteMessageById(int messageId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            rs = preparedStatement.executeQuery();

            int deletedRows = preparedStatement.executeUpdate();

            if (deletedRows != 0) {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                return message;
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return null;
        
    }

    public Message updateMessageText(int messageId, String newMessageText) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionUtil.getConnection();
            // Create a prepared statement with the SQL UPDATE statement
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
    
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

    public List<Message> getMessagesByUserId(int account_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        List<Message> messages = new ArrayList<>();
    
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);
            rs = preparedStatement.executeQuery();
    
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
