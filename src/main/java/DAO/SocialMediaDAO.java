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
        ResultSet rs = null;

        try {
            connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM message WHERE message_id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newMessageText);
            preparedStatement.setInt(2, messageId);
            rs = preparedStatement.executeQuery();

            int numberOfUpdatedRows = preparedStatement.executeUpdate();

            if(numberOfUpdatedRows != 0)
            {
                Message message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), 
                    rs.getString("message_text"), rs.getLong("time_posted_epoch"));

                return message;
            }
        }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        
        return null;
        
    }

    


    
}
