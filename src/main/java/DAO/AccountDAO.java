package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    /**
     * Adds a new account to the database.
     *
     * @param account The Account object to be added.
     * @return The newly added Account object with its generated account_id, or null if addition fails.
     */
    public Account addAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL query to insert a new account
            String sql = "INSERT INTO account(username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            // Execute the statement
            preparedStatement.executeUpdate();

            // Get the generated keys
            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                return new Account(keys.getInt(1), account.getUsername(), account.getPassword());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Retrieves an account from the database by its unique account_id.
     *
     * @param account_id The unique identifier of the account to retrieve.
     * @return The Account object with the specified account_id, or null if not found.
     */
    public Account getAccountById(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL query to select an account by account_id
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Verifies user credentials by checking if the username and password match a database record.
     *
     * @param account The Account object containing the username and password to verify.
     * @return The Account object with the account_id set if credentials are valid, or null if verification fails.
     */
    public Account verifyUserCredentials(Account account) {
        System.out.println("Account Object - Username: " + account.getUsername());
        System.out.println("Account Object - Password: " + account.getPassword());
        System.out.println("Account Object - Account ID: " + account.getAccount_id());

        Connection connection = ConnectionUtil.getConnection();
        try {
            // SQL query to select an account by username
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            // Set the username in the prepared statement
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
    
}
